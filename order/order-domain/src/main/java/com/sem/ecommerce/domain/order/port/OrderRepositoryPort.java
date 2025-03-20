package com.sem.ecommerce.domain.order.port;

import com.sem.ecommerce.domain.order.Order;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderRepositoryPort {
    Mono<Void> save(Order order);
    Mono<Order> findById(UUID id);
}
