package com.sem.ecommerce.payment.infra;

import com.sem.ecommerce.payment.domain.Payment;
import com.sem.ecommerce.payment.domain.PaymentServicePort;
import com.sem.ecommerce.payment.infra.repository.ProcessedEvent;
import com.sem.ecommerce.payment.infra.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedEventConsumer {

    private final PaymentServicePort paymentService;
    private final ProcessedEventRepository processedEventRepository;

    @Bean
    public Function<Flux<Message<String>>, Flux<Void>> orderCreatedProcessor() {
        return flux -> flux
                .doOnNext(message -> log.info("Received order created event: {}", message.getPayload()))
                .flatMap(this::processPayment)
                .onErrorContinue((error, obj) -> {
                    log.error("Error processing order payment: {}", error.getMessage(), error);
                });
    }

    @Transactional
    public Mono<Void> processPayment(Message<String> message) {
        UUID eventId = message.getHeaders().get("correlation-id", UUID.class);

        Payment payment = PaymentMapper.fromOrderCreatedMessage(message);

        return processedEventRepository.findById(eventId)
                .switchIfEmpty(processedEventRepository.save(new ProcessedEvent(eventId)))
                .flatMap(savedEvent -> paymentService.processPayment(payment))
                .then();
    }
}