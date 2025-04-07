package com.sem.ecommerce.payment.infra.event;

import com.sem.ecommerce.payment.domain.PaymentServicePort;
import com.sem.ecommerce.payment.infra.repository.ProcessedEventRepository;
import com.sem.ecormmerce.core.event.repository.DomainEventRepository;
import com.sem.ecormmerce.core.mapper.MapperUtils;
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

    private final PaymentServicePort paymentService;
    private final ProcessedEventRepository processedEventRepository;
    private final DomainEventRepository domainEventRepository;
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
        try {
            // 메시지 헤더에서 correlation-id 추출 (OrderCreatedEvent의 eventId)
            UUID orderEventId = message.getHeaders().get("correlation-id", UUID.class);

            // 원본 메시지에서 orderId 추출
            OrderCreatedEvent orderCreatedEvent = MapperUtils.fromJson(message.getPayload(), OrderCreatedEvent.class);
            UUID orderId = orderCreatedEvent.orderId();

            // 결제 실패 이벤트 생성
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(
                    UUID.randomUUID(),
                    orderId,
                    ZonedDateTime.now()
            );

            // 이벤트 저장 및 발행
            return domainEventRepository.save(paymentFailedEvent)
                    .doOnSuccess(v -> {
                        log.info("Payment failed event saved for order: {}", orderId);
                        // StreamBridge를 사용하여 결제 실패 이벤트 발행
                        publishPaymentFailedEvent(paymentFailedEvent);
                    });
        } catch (Exception e) {
            log.error("Error processing dead letter message: {}", e.getMessage(), e);
            return Mono.empty();
        }
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