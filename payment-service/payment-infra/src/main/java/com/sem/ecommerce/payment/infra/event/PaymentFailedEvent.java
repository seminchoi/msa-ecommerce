package com.sem.ecommerce.payment.infra.event;

import com.sem.ecommerce.core.event.DomainEvent;

import java.time.ZonedDateTime;
import java.util.UUID;

public record PaymentFailedEvent(
        UUID eventId,
        UUID orderId,
        ZonedDateTime occurredAt
) implements DomainEvent {
    public static final String EVENT_TYPE = "PAYMENT_FAILED";

    public static PaymentFailedEvent create(UUID orderId) {
        return new PaymentFailedEvent(
                UUID.randomUUID(),
                orderId,
                ZonedDateTime.now()
        );
    }

    @Override
    public String aggregateId() {
        return orderId.toString();
    }

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}