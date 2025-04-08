package com.sem.ecommerce.domain.order;

import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
public class OrderItems {
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
