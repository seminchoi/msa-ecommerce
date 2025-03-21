package com.sem.ecommerce.infra.event;


import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import com.sem.ecommerce.domain.order.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class RabbitBasicTest {
    private static final String EVENT_TYPE = "order.created";

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management-alpine");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.12-alpine");

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderCreatedEventPublisher publisher;

    @Value("${spring.cloud.stream.bindings.orderProcessor-out-0.destination}")
    private String exchangeName;

    @Value("${spring.cloud.stream.rabbit.bindings.orderProcessor-out-0.producer.routing-key-expression}")
    private String routingKey;

    @Test
    @DisplayName("rabbitmq connection 생성 및 메시지 전송이 수행이 성공한다.")
    void shouldSuccessConnect() {
        assertTrue(rabbitMQContainer.isRunning());
        assertNotNull(connectionFactory);

        // 실제 연결 테스트
        assertTrue(connectionFactory.createConnection().isOpen());

        // 간단한 메시지 전송 테스트
        rabbitTemplate.convertAndSend("test-exchange", "test-routing-key", "Hello RabbitMQ");
    }

    @Test
    @DisplayName("rabbitmq connection 생성 및 메시지 전송이 수행이 성공한다.")
    void shouldRetry_whenConsumerNack() throws Exception {
        OrderCreatedEvent testEvent = createSampleOrderCreatedEvent();

        // Lacth 를 사용해 Nack 횟수를 제한합니다.
        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger receiveCount = new AtomicInteger(0);

        // 이벤트를 전달받을 임시 큐를 생성합니다.
        String queueName = "test-order-queue-nack-" + UUID.randomUUID();
        rabbitTemplate.execute(channel -> {
            channel.exchangeDeclare(exchangeName, "topic", true);
            channel.queueDeclare(queueName, false, true, true, null);
            channel.queueBind(queueName, exchangeName, "order.created");
            return null;
        });

        // 이벤트를 전달받을 임시 Consumer 를 생성합니다.
        SimpleMessageListenerContainer container = createListner(queueName, receiveCount, latch);

        try {
            StepVerifier.create(publisher.publish(testEvent))
                    .verifyComplete();

            // 두 번 수신될 때까지 대기
            boolean messagesReceived = latch.await(10, TimeUnit.SECONDS);

            // 검증
            assertTrue(messagesReceived, "메시지가 두 번 수신되지 않았습니다");
            assertEquals(2, receiveCount.get(), "메시지가 재전송되지 않았습니다");
        } finally {
            // 리소스 정리
            container.stop();
            rabbitTemplate.execute(channel -> {
                channel.queueDelete(queueName);
                return null;
            });
        }
    }

    private @NotNull SimpleMessageListenerContainer createListner(String queueName, AtomicInteger receiveCount, CountDownLatch latch) {
        // 메시지 리스너 설정
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(
                rabbitTemplate.getConnectionFactory());
        container.setQueueNames(queueName);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            try {
                int count = receiveCount.incrementAndGet();
                log.info("Received message count: {}", count);

                if (count == 1) {
                    // 첫 번째 수신 시 nack 처리 (requeue=true)
                    log.info("First receive - sending NACK with requeue");
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                } else {
                    // 두 번째 수신 시 ack 처리
                    log.info("Second receive - sending ACK");
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                }

                latch.countDown();
            } catch (Exception e) {
                log.error("Error processing message", e);
                // 예외 발생 시 nack
                try {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        container.start();
        return container;
    }


    private OrderCreatedEvent createSampleOrderCreatedEvent() {
        return buildFixtureMonkey().giveMeBuilder(OrderCreatedEvent.class).sample();
    }

    private FixtureMonkey buildFixtureMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .defaultNotNull(true)
                .plugin(new JakartaValidationPlugin())
                .build();
    }
}
