package com.sem.ecommerce.domain.order;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderServicePort {
    Mono<UUID> createOrder(Order order);
}
