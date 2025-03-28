package com.sem.ecommerce.domain.order.event;

import com.sem.ecormmerce.core.event.DomainEvent;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        UUID orderId,
        UUID ordererId,
        long totalPrice,
        Receiver receiver,
        List<OrderItem> orderItems,
        ZonedDateTime occurredAt
) implements DomainEvent {
    private static final String EVENT_TYPE = "order.created";

    public record Receiver(
            String name,
            String phoneNumber,
            String address
    ) {

    }

    public record OrderItem(
            UUID id,
            UUID catalogId,
            long unitPrice,
            int quantity
    ) {
    }

    @Override
    public String aggregateId() {
        return ordererId().toString();
    }

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}