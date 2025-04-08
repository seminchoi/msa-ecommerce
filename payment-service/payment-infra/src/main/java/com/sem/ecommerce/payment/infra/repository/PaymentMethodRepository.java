package com.sem.ecommerce.payment.infra.repository;

import com.sem.ecommerce.payment.domain.PaymentMethod;
import com.sem.ecommerce.payment.domain.PaymentMethodRepositoryPort;
import com.sem.ecommerce.payment.infra.repository.converter.PaymentMethodR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentMethodRepository implements PaymentMethodRepositoryPort {
    private final PaymentMethodR2dbcRepository paymentMethodR2dbcRepository;

    @Override
    public Mono<Void> save(PaymentMethod paymentMethod) {
        PaymentMethodModel model = PaymentMethodModel.from(paymentMethod);
        return paymentMethodR2dbcRepository.save(model)
                .then();
    }

    @Override
    public Mono<PaymentMethod> findById(UUID id) {
        return paymentMethodR2dbcRepository.findById(id)
                .map(PaymentMethodModel::toDomain);
    }

    @Override
    public Flux<PaymentMethod> findByMemberId(String memberId) {
        return paymentMethodR2dbcRepository.findByMemberId(memberId)
                .map(PaymentMethodModel::toDomain);
    }
}
