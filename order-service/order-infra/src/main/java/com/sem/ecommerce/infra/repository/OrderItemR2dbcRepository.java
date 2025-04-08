package com.sem.ecommerce.infra.repository;

import com.sem.ecommerce.infra.repository.model.OrderItemModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface OrderItemR2dbcRepository extends R2dbcRepository<OrderItemModel, UUID> {
    Flux<OrderItemModel> findAllByOrderId(UUID orderId);
}
