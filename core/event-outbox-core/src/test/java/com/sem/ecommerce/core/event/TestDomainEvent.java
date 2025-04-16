package com.sem.ecommerce.core.event;

import java.time.ZonedDateTime;
import java.util.UUID;

public record TestDomainEvent(
        UUID eventId,
        String message,
        UUID entityId,
        ZonedDateTime occurredAt
) implements DomainEvent {

    public static TestDomainEvent create(String message) {
        return new TestDomainEvent(
                UUID.randomUUID(),
                message,
                UUID.randomUUID(),
                ZonedDateTime.now()
        );
    }

    @Override
    public String aggregateId() {
        return entityId.toString();
    }

    @Override
    public String eventType() {
        return "TEST_EVENT";
    }
}