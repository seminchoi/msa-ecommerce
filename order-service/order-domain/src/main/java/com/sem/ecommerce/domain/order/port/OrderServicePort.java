package com.sem.ecommerce.domain.order.port;

import com.sem.ecommerce.domain.order.Order;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderServicePort {
    Mono<UUID> createOrder(Order order);
}
