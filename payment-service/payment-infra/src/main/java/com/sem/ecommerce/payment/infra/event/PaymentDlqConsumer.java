package com.sem.ecommerce.payment.infra.event;

import com.sem.ecommerce.core.event.outbox.DomainEventRepository;
import com.sem.ecommerce.core.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class PaymentDlqConsumer {
    private final DomainEventRepository domainEventRepository;
    private final ProcessedEventRepository processedEventRepository;

    @Bean
    public Consumer<Flux<Message<String>>> paymentDlqConsumer() {
        return flux -> flux
                .doOnNext(message -> log.info("Processing dead letter message: {}", message.getPayload()))
                .flatMap(this::processFailedPayment)
                .subscribe();
    }

    private Mono<Void> processFailedPayment(Message<String> message) {
        // 메시지 헤더에서 correlation-id 추출 (OrderCreatedEvent의 eventId)
        UUID eventId = message.getHeaders().get("correlation-id", UUID.class);

        // 원본 메시지에서 orderId 추출
        OrderCreatedEvent orderCreatedEvent = MapperUtils.fromJson(message.getPayload(), OrderCreatedEvent.class);
        UUID orderId = orderCreatedEvent.orderId();

        // 결제 실패 이벤트 생성
        PaymentFailedEvent paymentFailedEvent = PaymentFailedEvent.create(orderId);

        return processedEventRepository.findById(eventId)
                .switchIfEmpty(processedEventRepository.save(new ProcessedEvent(eventId)))
                .then(domainEventRepository.publish(paymentFailedEvent))
                .then();
    }
}