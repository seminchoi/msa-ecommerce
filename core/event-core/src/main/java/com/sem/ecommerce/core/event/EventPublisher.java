package com.sem.ecommerce.core.event;

import reactor.core.publisher.Mono;

import java.util.List;

public interface EventPublisher {
    Mono<Void> publish(DomainEvent event);
    Mono<Void> publishAll(List<DomainEvent> events);
}