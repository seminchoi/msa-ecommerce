package com.sem.ecommerce.payment.infra.event;

import com.sem.ecommerce.core.mapper.MapperUtils;
import com.sem.ecommerce.payment.domain.Payment;
import com.sem.ecommerce.payment.domain.PaymentServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedEventConsumer {
    private final PaymentServicePort paymentService;
    private final ProcessedEventRepository processedEventRepository;

    @Bean
    public Function<Flux<Message<String>>, Flux<Void>> orderCreatedConsumer() {
        return flux -> flux
                .flatMap(this::consumeEvent)
                .onErrorContinue((error, obj) -> {
                    log.error("Error processing order payment: {}", error.getMessage(), error);
                });
    }

    private Mono<Void> consumeEvent(Message<String> message) {
        OrderCreatedEvent event = MapperUtils.fromJson(message.getPayload(), OrderCreatedEvent.class);

        return processedEventRepository.findById(event.eventId())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.empty();
                    }
                    return processedEventRepository.save(new ProcessedEvent(event.eventId()))
                            .then(paymentService.processPayment(createPayment(event)))
                            .then();
                });
    }

    private Payment createPayment(OrderCreatedEvent event) {
        return Payment.create(
                event.orderId(),
                event.ordererId(),
                event.paymentMethodId(),
                event.totalPrice()
        );
    }
}