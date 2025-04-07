package com.sem.ecommerce.payment.infra.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PaymentR2dbcRepository extends R2dbcRepository<PaymentModel, UUID> {
    Flux<PaymentModel> findByMemberId(UUID memberId);
    Flux<PaymentModel> findByOrderId(UUID orderId);
}
