package com.sem.ecommerce.payment.infra.event;

import com.sem.ecommerce.core.event.EventType;
import lombok.Getter;

@Getter
public enum PaymentEventType implements EventType {
    PAYMENT_COMPLETED("payment.created", "paymentProcessor-out-0"),
    PAYMENT_FAILED("payment.failed", "paymentProcessor-out-0");

    private final String eventKey;
    private final String outputBinding;

    PaymentEventType(String eventKey, String outputBinding) {
        this.eventKey = eventKey;
        this.outputBinding = outputBinding;
    }
}
