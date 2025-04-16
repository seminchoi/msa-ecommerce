package com.sem.ecommerce.core.event;

import com.redis.testcontainers.RedisContainer;
import com.sem.ecommerce.core.event.outbox.OutBoxCache;
import com.sem.ecommerce.core.event.outbox.OutBoxEventPublisher;
import com.sem.ecommerce.core.event.outbox.OutBoxEventRepository;
import com.sem.ecommerce.core.event.outbox.OutboxEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {OutboxIntegrationTestConfig.class})
public class PublishConfirmTest {
    @Container
    @ServiceConnection
    public static final RabbitMQContainer RABBIT_MQ_CONTAINER =
            new RabbitMQContainer("rabbitmq:3.8-management-alpine");

    @Container
    @ServiceConnection
    public static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));

    @Autowired
    private OutBoxEventRepository outBoxEventRepository;

    @Autowired
    private OutBoxCache outBoxCache;

    @Autowired
    private AmqpAdmin amqpAdmin;


    @Autowired
    private OutBoxEventPublisher outBoxEventPublisher;

    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisStringTemplate;

    private final String EXCHANGE_NAME = "test-exchange";
    private final String ROUTING_KEY = "test.event";
    private OutboxEvent testEvent;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());

        registry.add("spring.rabbitmq.host", RABBIT_MQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", () -> RABBIT_MQ_CONTAINER.getMappedPort(5672));
        registry.add("spring.rabbitmq.username", () -> "guest");
        registry.add("spring.rabbitmq.password", () -> "guest");
    }


    @BeforeEach
    void setUp() {
        // Set up RabbitMQ exchange
        amqpAdmin.declareExchange(new TopicExchange(EXCHANGE_NAME, true, false));

        Queue queue = new Queue("test-queue", true);
        amqpAdmin.declareQueue(queue);

        // 큐를 Exchange에 바인딩
        Binding binding = BindingBuilder.bind(queue)
                .to(new TopicExchange(EXCHANGE_NAME))
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        // Create test event
        testEvent = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .aggregateId("test-aggregate-123")
                .eventKey(ROUTING_KEY)
                .outputBinding("testProcessor-out-0")
                .expirationMinutes(30)
                .payload("{\"message\":\"Test message content\"}")
                .status(OutboxEvent.OutboxStatus.PENDING)
                .occurredAt(java.time.ZonedDateTime.now())
                .isNew(true)
                .build();

        // Clear Redis cache
        reactiveRedisStringTemplate.delete("order:events:acked").block();
    }

    @Test
    @DisplayName("이벤트가 발행되고, ACK를 받으면 Redis에 correlation key가 저장된다")
    void shouldSaveCorrelationIdToRedis_whenEventIsPublishedAndAcknowledged() throws Exception {
        // Given
        outBoxEventRepository.save(testEvent).block();

        outBoxEventPublisher.publish().block();

        TimeUnit.SECONDS.sleep(3);

        // Verify correlation ID was saved to Redis
        StepVerifier.create(outBoxCache.getAllAckedOutboxEvents())
                .assertNext(eventId -> {
                    assertThat(testEvent.getId()).isEqualTo(eventId);
                })
                .verifyComplete();
    }


    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    @DisplayName("RabbitTemplate을 직접 사용하여 메시지를 발행하고 ACK가 수신되는지 확인")
    void shouldReceiveAck_whenSendingDirectlyWithRabbitTemplate() throws Exception {
        // Given
        String correlationId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(correlationId);
        String testMessage = "{\"message\":\"Direct test message\"}";

        // 비동기 확인을 위한 CountDownLatch
        CountDownLatch confirmLatch = new CountDownLatch(1);
        AtomicBoolean wasAcked = new AtomicBoolean(false);


        // When - 메시지 직접 전송
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, testMessage, correlationData);

        // Then - ACK 수신 대기
        boolean receivedAck = confirmLatch.await(5, TimeUnit.SECONDS);

        // 검증
        assertThat(receivedAck).isTrue();
        assertThat(wasAcked.get()).isTrue();

        // OutBoxCache에 correlationId가 추가되었는지 확인
        // 이 시점에서는 ACK가 받아졌으므로 Redis에 추가하는 테스트를 수행
        outBoxCache.addAckedOutboxEvent(correlationId).block();

        // Redis에서 값을 읽어와 확인
        StepVerifier.create(outBoxCache.getAllAckedOutboxEvents())
                .expectNextMatches(id -> id.equals(correlationId))
                .verifyComplete();
    }
//    @Test
//    @DisplayName("Redis에 저장된 ACK 이벤트는 다음 발행 시 DB에서 SENT 상태로 변경된다")
//    void shouldUpdateSentStatusInDb_whenAckedEventIdsAreInRedis() {
//        // Given - Save event to DB
//        outBoxEventRepository.save(testEvent).block();
//
//        // Add event ID to Redis ACK cache
//        outBoxCache.addAckedOutboxEvent(testEvent.getId().toString()).block();
//
//        // When - Run the publisher
//        outBoxEventPublisher.publish().block();
//
//        // Then - Verify event status is updated to SENT in DB
//        StepVerifier.create(outBoxEventRepository.findById(testEvent.getId()))
//                .assertNext(event -> {
//                    assertThat(event.getStatus()).isEqualTo(OutboxEvent.OutboxStatus.SENT);
//                })
//                .verifyComplete();
//
//        // Verify Redis cache is cleared
//        StepVerifier.create(outBoxCache.getAllAckedOutboxEvents())
//                .expectNextCount(0)
//                .verifyComplete();
//    }
}