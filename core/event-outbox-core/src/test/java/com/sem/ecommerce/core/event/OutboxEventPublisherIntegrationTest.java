package com.sem.ecommerce.core.event;

import com.sem.ecommerce.core.event.outbox.OutBoxEventRepository;
import com.sem.ecommerce.core.event.outbox.OutboxEvent;
import com.sem.ecommerce.core.event.outbox.OutboxEventPublisherApdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {OutboxIntegrationTestConfig.class})
@Testcontainers
public class OutboxEventPublisherIntegrationTest {

    @Autowired
    private OutboxEventPublisherApdapter eventPublisher;

    @Autowired
    private OutBoxEventRepository outBoxEventRepository;

    @Test
    @DisplayName("도메인 이벤트가 성공적으로 발행되고 저장되어야 한다")
    void shouldPersistEvent_whenPublish() {
        // Given
        TestDomainEvent event = TestDomainEvent.create("Test message for integration test");

        // When
        Mono<Void> result = eventPublisher.publish(event);

        // Then
        StepVerifier.create(result)
                .expectComplete()
                .verify(Duration.ofSeconds(5));

        StepVerifier.create(outBoxEventRepository.findById(event.eventId()))
                .assertNext(outboxEvent -> {
                    assertThat(outboxEvent).isNotNull();
                    assertThat(outboxEvent.getId()).isEqualTo(event.eventId());
                    assertThat(outboxEvent.getEventKey()).isEqualTo(event.eventType());
                    assertThat(outboxEvent.getStatus()).isEqualTo(OutboxEvent.OutboxStatus.PENDING);
                })
                .expectComplete()
                .verify(Duration.ofSeconds(5));
    }

//    @Test
//    @DisplayName("OutBoxEventPublisher가 저장된 이벤트를 발행해야 한다")
//    void shouldPublishStoredEvents() {
//        // Given
//        TestDomainEvent event = TestDomainEvent.create("Test message for scheduled publishing");
//        OutboxEvent outboxEvent = createOutboxEvent(event);
//
//        // 이벤트 저장
//        outBoxEventRepository.save(outboxEvent).block();
//
//        // When
//        OutBoxEventPublisher publisher = new OutBoxEventPublisher(
//                new OutBoxQueryRepository(null), // 실제 구현에서는 적절한 R2dbcEntityTemplate 필요
//                new OutBoxCache(null), // 실제 구현에서는 적절한 ReactiveRedisTemplate 필요
//                new StreamBridge() {
//                    @Override
//                    public boolean send(String bindingName, Object data) {
//                        // 메시지 캡처 로직 구현
//                        return true;
//                    }
//                }
//        );
//
//        Mono<Void> result = publisher.publish();
//
//        // Then
//        StepVerifier.create(result)
//                .expectComplete()
//                .verify(Duration.ofSeconds(5));
//
//        // 이벤트가 StreamBridge를 통해 발행되었는지 확인
//        // (실제 통합 테스트에서는 OutputDestination 또는 다른 방법으로 확인)
//    }
//
//    private OutboxEvent createOutboxEvent(DomainEvent event) {
//        return OutboxEvent.builder()
//                .id(event.eventId())
//                .aggregateId(event.aggregateId())
//                .eventKey(event.eventType())
//                .outputBinding("testProcessor-out-0")
//                .expirationMinutes(30)
//                .payload("{\"message\":\"" + ((TestDomainEvent)event).message() + "\"}")
//                .status(OutboxEvent.OutboxStatus.PENDING)
//                .occurredAt(event.occurredAt())
//                .build();
//    }
}