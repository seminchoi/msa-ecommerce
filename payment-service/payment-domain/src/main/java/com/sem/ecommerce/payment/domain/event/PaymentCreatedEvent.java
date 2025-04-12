package com.sem.ecommerce.payment.domain.event;

import com.sem.ecommerce.core.event.DomainEvent;
import com.sem.ecommerce.payment.domain.Payment;
import com.sem.ecommerce.payment.domain.PaymentStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

public record PaymentCreatedEvent(
        UUID eventId,
        UUID paymentId,
        UUID orderId,
        UUID memberId,
        UUID paymentMethodId,
        long amount,
        PaymentStatus status,
        ZonedDateTime occurredAt
) implements DomainEvent {
    private static final String EVENT_TYPE = "PAYMENT_CREATED";

    public static PaymentCreatedEvent from(Payment payment) {
        return new PaymentCreatedEvent(
                UUID.randomUUID(),
                payment.getId(),
                payment.getOrderId(),
                payment.getMemberId(),
                payment.getPaymentMethodId(),
                payment.getAmount(),
                payment.getStatus(),
                ZonedDateTime.now()
        );
    }

    @Override
    public String aggregateId() {
        return paymentId.toString();
    }

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}