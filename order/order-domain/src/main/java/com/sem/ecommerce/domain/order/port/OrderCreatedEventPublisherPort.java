package com.sem.ecommerce.domain.order.port;

import com.sem.ecommerce.domain.order.event.OrderCreatedEvent;
import com.sem.ecommerce.core.event.EventPublisher;

public interface OrderCreatedEventPublisherPort extends EventPublisher<OrderCreatedEvent> {
}
