package com.sem.ecommerce.domain.order;

import com.sem.ecommerce.domain.order.event.OrderCreatedEvent;
import com.sem.ecommerce.core.event.DomainEventArchive;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@Builder
public class Order {
    @NotNull
    private UUID id;

    @NotNull
    private UUID ordererId;

    @Valid
    @NotNull
    private Receiver receiver;

    @NotNull
    private OrderState orderState;

    @Valid
    @NotNull
    private OrderItems orderItems;

    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Builder.Default
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @Delegate
    @Builder.Default
    private DomainEventArchive archive = new DomainEventArchive();

    public long calculateTotalPrice() {
        return orderItems.calculateTotalPrice();
    }

    public static Order create(UUID ordererId, Receiver receiver, OrderItems orderItems) {
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .id(orderId)
                .ordererId(ordererId)
                .receiver(receiver)
                .orderItems(orderItems)
                .orderState(OrderState.NONE)
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();

        OrderCreatedEvent event = OrderCreatedEvent.from(order);
        order.register(event);

        return order;
    }
}
