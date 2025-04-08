package com.sem.ecommerce.payment.infra.repository;

import com.sem.ecommerce.core.event.outbox.DomainEventRepository;
import com.sem.ecommerce.payment.domain.Payment;
import com.sem.ecommerce.payment.domain.PaymentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentRepository implements PaymentRepositoryPort {

    private final PaymentR2dbcRepository paymentR2dbcRepository;
    private final DomainEventRepository domainEventRepository;

    @Override
    public Mono<Void> save(Payment payment) {
        PaymentModel model = PaymentModel.from(payment, true);

        return paymentR2dbcRepository.save(model)
                .then(domainEventRepository.publishAll(payment.getEvents())) // 이벤트 저장 로직 추가
                .then(Mono.fromRunnable(payment::clearEvents)) // 이벤트 초기화
                .then();
    }

    @Override
    public Mono<Payment> findById(UUID id) {
        return paymentR2dbcRepository.findById(id)
                .map(PaymentModel::toDomain);
    }

    @Override
    public Flux<Payment> findByOrderId(UUID orderId) {
        return paymentR2dbcRepository.findByOrderId(orderId)
                .map(PaymentModel::toDomain);
    }
}