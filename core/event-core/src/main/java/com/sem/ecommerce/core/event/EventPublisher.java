package com.sem.ecommerce.core.event;

import reactor.core.publisher.Mono;

public interface EventPublisher<T extends DomainEvent> {
    Mono<Void> publish(T event);
}
