package com.sem.ecommerce.payment.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentMethodRepositoryPort {
    Mono<Void> save(PaymentMethod paymentMethod);
    Mono<PaymentMethod> findById(UUID id);
    Flux<PaymentMethod> findByMemberId(String memberId);
}