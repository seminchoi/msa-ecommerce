package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.core.event.outbox.EventType;
import lombok.Getter;

@Getter
public enum OrderEventType implements EventType {
    ORDER_CREATED("order.created", "orderProcessor-out-0", 10);

    private final String eventKey;
    private final String outputBinding;
    private final int expirationTimeInMinutes;

    OrderEventType(String eventKey, String outputBinding, int expirationTimeInMinutes) {
        this.eventKey = eventKey;
        this.outputBinding = outputBinding;
        this.expirationTimeInMinutes = expirationTimeInMinutes;
    }
}