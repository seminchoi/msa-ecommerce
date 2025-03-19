package com.sem.ecormmerce.core.event;

import reactor.core.publisher.Mono;

public interface EventPublisher<T extends DomainEvent> {
    Mono<Void> publish(T event);
}
