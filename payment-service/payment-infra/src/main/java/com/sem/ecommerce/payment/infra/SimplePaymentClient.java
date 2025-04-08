package com.sem.ecommerce.payment.infra;

import com.sem.ecommerce.payment.domain.Payment;
import com.sem.ecommerce.payment.domain.PaymentClientPort;
import com.sem.ecommerce.payment.domain.PaymentMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SimplePaymentClient implements PaymentClientPort {
    @Override
    public Mono<Void> processPayment(Payment payment, PaymentMethod paymentMethod) {
        return Mono.empty();
    }
}
