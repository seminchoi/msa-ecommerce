package com.sem.ecommerce.core.event;

import com.sem.ecommerce.core.event.outbox.EventType;

public enum TestEventType implements EventType {
    TEST_EVENT("test.event", "testProcessor-out-0", 30);

    private final String eventKey;
    private final String outputBinding;
    private final int expirationTimeInMinutes;

    TestEventType(String eventKey, String outputBinding, int expirationTimeInMinutes) {
        this.eventKey = eventKey;
        this.outputBinding = outputBinding;
        this.expirationTimeInMinutes = expirationTimeInMinutes;
    }

    @Override
    public String getEventKey() {
        return eventKey;
    }

    @Override
    public String getOutputBinding() {
        return outputBinding;
    }

    @Override
    public int getExpirationTimeInMinutes() {
        return expirationTimeInMinutes;
    }
}