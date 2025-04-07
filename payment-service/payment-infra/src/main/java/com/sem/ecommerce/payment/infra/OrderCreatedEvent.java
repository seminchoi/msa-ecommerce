package com.sem.ecommerce.payment.infra;

import java.time.ZonedDateTime;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        UUID orderId,
        UUID ordererId,
        long totalPrice,
        ZonedDateTime occurredAt
) {
}