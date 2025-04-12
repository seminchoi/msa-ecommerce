package com.sem.ecommerce.payment.infra.event;

import com.sem.ecommerce.core.event.outbox.EventType;
import com.sem.ecommerce.core.event.outbox.EventTypeHolder;
import lombok.Getter;

@Getter
public enum PaymentEventType implements EventType {
    PAYMENT_COMPLETED("payment.created", "paymentProcessor-out-0", -1),
    PAYMENT_FAILED("payment.failed", "paymentProcessor-out-0", -1);

    private final String eventKey;
    private final String outputBinding;
    private final int expirationTimeInMinutes;

    PaymentEventType(String eventKey, String outputBinding, int expirationTimeInMinutes) {
        this.eventKey = eventKey;
        this.outputBinding = outputBinding;
        this.expirationTimeInMinutes = expirationTimeInMinutes;
    }
}