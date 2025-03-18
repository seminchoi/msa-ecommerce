package com.sem.ecommerce.domain.order;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderRepository {
    Mono<Void> save(Order order);
    Mono<Order> findById(UUID id);
}
