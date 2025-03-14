package com.sem.ecommerce.infra.repository.model;

import com.sem.ecommerce.domain.order.Order;
import com.sem.ecommerce.domain.order.OrderItem;
import com.sem.ecommerce.domain.order.Receiver;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class OrderModelMapper {
    public static OrderModel from(Order order) {
        Receiver receiver = order.getReceiver();
        return OrderModel.builder()
                .id(order.getId())
                .ordererId(order.getOrdererId())
                .orderState(order.getOrderState())
                .receiverAddress(receiver.address())
                .receiverName(receiver.name())
                .receiverPhoneNumber(receiver.phoneNumber())
                .build();
    }

    public static List<OrderItemModel> fromOrder(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        return orderItems.stream().map(o -> OrderModelMapper.from(order.getId(), o)).collect(Collectors.toList());
    }

    public static OrderItemModel from(UUID orderId, OrderItem orderItem) {
        return OrderItemModel.builder()
                .id(orderItem.getId())
                .orderId(orderId)
                .catalogId(orderItem.getCatalogId())
                .unitPrice(orderItem.getUnitPrice())
                .quantity(orderItem.getQuantity())
                .shippingState(orderItem.getShippingState())
                .orderItemState(orderItem.getOrderItemState())
                .build();
    }

    public static List<OrderItemModel> fromList(List<OrderItem> orderItems) {
        return orderItems.stream().map(OrderModelMapper::orderItemFrom).collect(Collectors.toList());
    }


    public static Order toDomainWith(OrderModel orderModel, List<OrderItemModel> orderItems) {
        return Order.builder()
                .id(orderModel.id())
                .ordererId(orderModel.ordererId())
                .orderState(orderModel.orderState())
                .orderItems(orderItemsTo(orderItems))
                .build();
    }


    public static OrderItemModel orderItemFrom(OrderItem orderItem) {
        return OrderItemModel.builder()
                .id(orderItem.getId())
                .catalogId(orderItem.getCatalogId())
                .unitPrice(orderItem.getUnitPrice())
                .shippingState(orderItem.getShippingState())
                .orderItemState(orderItem.getOrderItemState())
                .build();
    }

    public List<OrderItem> orderItemsTo(List<OrderItemModel> models) {
        return models.stream().map(OrderModelMapper::orderItemTo).toList();
    }

    public static OrderItem orderItemTo(OrderItemModel model) {
        return OrderItem.builder()
                .id(model.id())
                .catalogId(model.catalogId())
                .unitPrice(model.unitPrice())
                .quantity(model.quantity())
                .shippingState(model.shippingState())
                .orderItemState(model.orderItemState())
                .build();
    }
}
