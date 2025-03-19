package com.sem.ecormmerce.core.event;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface DomainEvent {
    UUID eventId();
    ZonedDateTime occurredAt();
}
