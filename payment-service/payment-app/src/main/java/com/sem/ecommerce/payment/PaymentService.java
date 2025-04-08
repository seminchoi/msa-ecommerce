package com.sem.ecommerce.payment;

import com.sem.ecommerce.core.event.EventPublisher;
import com.sem.ecommerce.payment.domain.Payment;
import com.sem.ecommerce.payment.domain.PaymentClientPort;
import com.sem.ecommerce.payment.domain.PaymentMethodRepositoryPort;
import com.sem.ecommerce.payment.domain.PaymentRepositoryPort;
import com.sem.ecommerce.payment.domain.PaymentServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentServicePort {
    private final PaymentClientPort paymentClient;
    private final PaymentRepositoryPort paymentRepository;
    private final PaymentMethodRepositoryPort paymentMethodRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Mono<Void> processPayment(Payment payment) {
        return paymentMethodRepository.findById(payment.getPaymentMethodId())
                .flatMap(paymentMethod -> paymentClient.processPayment(payment, paymentMethod))
                .then(paymentRepository.save(payment))
                .then(eventPublisher.publishAll(payment.getEvents()))
                .then(Mono.fromRunnable(payment::clearEvents))
                .then();
    }
}
