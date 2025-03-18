package com.sem.ecommerce.domain.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
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

    @Builder
    public Order(UUID id, UUID ordererId, Receiver receiver, OrderState orderState, OrderItems orderItems) {
        this.id = id;
        this.ordererId = ordererId;
        this.receiver = receiver;
        this.orderState = orderState;
        this.orderItems = orderItems;
    }
}
