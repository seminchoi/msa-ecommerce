package com.sem.ecommerce.domain.order;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class Order {
    private UUID id;
    private UUID ordererId;
    private UUID userId;
    private ReceiverDetails receiverDetails;
    private OrderState orderState;
    private List<OrderItem> orderItems;

    @Builder
    public Order(UUID id, UUID ordererId, UUID userId, ReceiverDetails receiverDetails, OrderState orderState, List<OrderItem> orderItems) {
        this.id = id;
        this.ordererId = ordererId;
        this.userId = userId;
        this.receiverDetails = receiverDetails;
        this.orderState = orderState;
        this.orderItems = orderItems;
    }
}
