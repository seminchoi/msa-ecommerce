package com.sem.ecommerce.core.event;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface DomainEvent {
    UUID eventId();
    String aggregateId();
    String eventType();
    ZonedDateTime occurredAt();
}
