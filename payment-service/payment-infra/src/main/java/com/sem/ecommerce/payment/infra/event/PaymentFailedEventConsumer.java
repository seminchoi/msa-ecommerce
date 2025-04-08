package com.sem.ecommerce.payment.infra.event;

import com.sem.ecommerce.core.event.outbox.DomainEventRepository;
import com.sem.ecommerce.core.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class PaymentFailedEventConsumer {
    private final DomainEventRepository domainEventRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final StreamBridge streamBridge;
    private static final String PAYMENT_FAILED_BINDING = "orderProcessor-out-0";

    @Bean
    public Consumer<Flux<Message<String>>> orderCreatedDlqProcessor() {
        return flux -> flux
                .doOnNext(message -> log.info("Processing dead letter message: {}", message.getPayload()))
                .flatMap(this::processFailedPayment)
                .onErrorContinue((error, obj) -> {
                    log.error("Error processing dead letter message: {}", error.getMessage(), error);
                })
                .subscribe();
    }

    private Mono<Void> processFailedPayment(Message<String> message) {
        // 메시지 헤더에서 correlation-id 추출 (OrderCreatedEvent의 eventId)
        UUID eventId = message.getHeaders().get("correlation-id", UUID.class);

        // 원본 메시지에서 orderId 추출
        OrderCreatedEvent orderCreatedEvent = MapperUtils.fromJson(message.getPayload(), OrderCreatedEvent.class);
        UUID orderId = orderCreatedEvent.orderId();

        // 결제 실패 이벤트 생성
        PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(
                UUID.randomUUID(),
                orderId,
                ZonedDateTime.now()
        );

        return processedEventRepository.findById(eventId)
                .switchIfEmpty(processedEventRepository.save(new ProcessedEvent(eventId)))
                .then(domainEventRepository.publish(paymentFailedEvent))
                .doOnSuccess(v -> publishPaymentFailedEvent(paymentFailedEvent))
                .then();
    }


    private void publishPaymentFailedEvent(PaymentFailedEvent event) {
        try {
            Message<String> message = MessageBuilder
                    .withPayload(MapperUtils.toJson(event))
                    .setHeader("correlation-id", event.eventId().toString())
                    .setHeader("event-type", event.eventType())
                    .build();

            boolean result = streamBridge.send(PAYMENT_FAILED_BINDING, message);
            if (result) {
                log.info("Payment failed event published for order: {}", event.orderId());
            } else {
                log.error("Failed to publish payment failed event for order: {}", event.orderId());
            }
        } catch (Exception e) {
            log.error("Error publishing payment failed event: {}", e.getMessage(), e);
        }
    }
}