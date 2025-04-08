package com.sem.ecommerce.payment.domain;

import reactor.core.publisher.Mono;

public interface PaymentClientPort {
    Mono<Void> processPayment(Payment payment, PaymentMethod paymentMethod);
}
