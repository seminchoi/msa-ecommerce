package com.sem.ecommerce.infra.repository.model;

import com.sem.ecommerce.domain.order.Order;
import com.sem.ecommerce.domain.order.OrderItem;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrderModelComposite(
        OrderModel order,
        List<OrderItemModel> orderItems
) {
    public Order toDomain() {
        return Order.builder()
                .id(order.getId())
                .ordererId(order.getOrdererId())
                .orderState(order.getOrderState())
                .orderItems(toOrderItems(orderItems))
                .build();
    }

    private List<OrderItem> toOrderItems(List<OrderItemModel> orderItems) {
        return orderItems.stream().map(OrderItemModel::toDomain).toList();
    }

    public static OrderModelComposite from(Order order) {
        return from(order, false);
    }

    public static OrderModelComposite from(Order order, boolean isNew) {
        return OrderModelComposite.builder()
                .order(OrderModel.from(order, isNew))
                .orderItems(fromOrderItems(order.getId(), order.getOrderItems(), isNew))
                .build();
    }

    private static List<OrderItemModel> fromOrderItems(UUID orderId, List<OrderItem> orderItems, boolean isNew) {
        return orderItems.stream().map(it -> OrderItemModel.from(orderId, it, isNew)).toList();
    }
}
