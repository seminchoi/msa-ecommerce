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

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(orderItems);
    }
}
