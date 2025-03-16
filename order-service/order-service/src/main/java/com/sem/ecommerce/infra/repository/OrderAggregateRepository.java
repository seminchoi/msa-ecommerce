package com.sem.ecommerce.infra.repository;

import com.sem.ecommerce.domain.order.Order;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderAggregateRepository {
    Mono<Void> save(Order order);
    Mono<Order> findById(UUID orderId);
}
