package com.sem.ecommerce.payment.domain;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface PaymentServicePort {
    Mono<Void> processPayment(Payment payment);
}
