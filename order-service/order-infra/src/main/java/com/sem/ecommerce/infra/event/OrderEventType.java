package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.core.event.EventType;

public enum OrderEventType implements EventType {
    ORDER_CREATED("order.created", "orderProcessor-out-0");

    private final String eventType;
    private final String outputBinding;

    OrderEventType(String eventType, String outputBinding) {
        this.eventType = eventType;
        this.outputBinding = outputBinding;
    }

    @Override
    public String getEventKey() {
        return eventType;
    }

    @Override
    public String getOutputBinding() {
        return outputBinding;
    }
}
