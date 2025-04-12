package com.sem.ecommerce.domain.order.event;

import com.sem.ecommerce.core.event.DomainEvent;
import com.sem.ecommerce.domain.order.Order;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record OrderCreatedEvent(
        UUID eventId,
        UUID orderId,
        UUID ordererId,
        long totalPrice,
        Receiver receiver,
        List<OrderItem> orderItems,
        ZonedDateTime occurredAt
) implements DomainEvent {
    private static final String EVENT_TYPE = "ORDER_CREATED";

    public record Receiver(
            String name,
            String phoneNumber,
            String address
    ) {
    }

    public record OrderItem(
            UUID id,
            UUID catalogId,
            long unitPrice,
            int quantity
    ) {
    }

    public static OrderCreatedEvent from(Order order) {
        // OrderItem을 OrderCreatedEvent.OrderItem으로 변환
        List<OrderItem> eventItems = order.getOrderItems().getOrderItems().stream()
                .map(item -> new OrderItem(
                        item.getId(),
                        item.getCatalogId(),
                        item.getUnitPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        // Receiver를 OrderCreatedEvent.Receiver로 변환
        Receiver eventReceiver = new Receiver(
                order.getReceiver().name(),
                order.getReceiver().phoneNumber(),
                order.getReceiver().address()
        );

        return new OrderCreatedEvent(
                UUID.randomUUID(),
                order.getId(),
                order.getOrdererId(),
                order.getOrderItems().calculateTotalPrice(),
                eventReceiver,
                eventItems,
                ZonedDateTime.now()
        );
    }

    @Override
    public String aggregateId() {
        return ordererId().toString();
    }

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}