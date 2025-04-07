package com.sem.ecommerce.payment.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentRepositoryPort {
    Mono<Void> save(Payment payment);
    Mono<Payment> findById(UUID id);
    Flux<Payment> findByOrderId(UUID orderId);
}