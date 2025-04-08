package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.core.event.EventType;
import lombok.Getter;

@Getter
public enum OrderEventType implements EventType {
    ORDER_CREATED("order.created", "orderProcessor-out-0");

    private final String eventKey;
    private final String outputBinding;

    OrderEventType(String eventKey, String outputBinding) {
        this.eventKey = eventKey;
        this.outputBinding = outputBinding;
    }
}
