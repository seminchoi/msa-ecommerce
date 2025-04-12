package com.sem.ecommerce.domain.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
public class OrderItems {
    @NotEmpty
    private List<OrderItem> orderItems;

    public OrderItems(List<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>(orderItems);
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public long calculateTotalPrice() {
        return orderItems.stream().mapToLong(OrderItem::calculatePrice).sum();
    }
}
