package com.sem.ecommerce.payment.infra.repository.converter;

import com.sem.ecommerce.payment.infra.repository.PaymentMethodModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PaymentMethodR2dbcRepository extends R2dbcRepository<PaymentMethodModel, UUID> {
    Flux<PaymentMethodModel> findByMemberId(String memberId);
}
