package com.sem.ecommerce.infra.event;

public enum OutBoxProcessorState {
    HEALTHY,
    FAILED,
    HALF_OPEN,
    RECOVERING
}
