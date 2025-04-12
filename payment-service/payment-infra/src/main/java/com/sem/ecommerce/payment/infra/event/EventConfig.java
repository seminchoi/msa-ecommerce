package com.sem.ecommerce.payment.infra.event;

import com.sem.ecommerce.core.event.outbox.EventTypeHolder;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {
    @PostConstruct
    public void registerEventTypes() {
        EventTypeHolder.registerEventTypes(PaymentEventType.class);
    }
}
