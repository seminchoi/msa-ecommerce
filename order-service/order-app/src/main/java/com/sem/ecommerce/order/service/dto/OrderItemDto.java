package com.sem.ecommerce.order.service.dto;

import com.sem.ecommerce.domain.order.OrderItem;
import com.sem.ecommerce.domain.order.OrderItemState;
import com.sem.ecommerce.domain.order.ShippingState;

import java.util.UUID;

public record OrderItemDto(
        UUID productId,
        int quantity
) {
    public OrderItem toDomain() {
        return OrderItem.builder()
                .id(UUID.randomUUID())
                .catalogId(productId())
                .quantity(quantity())
                .unitPrice(0) // 가격 정보는 제품 서비스에서 가져와야 함
                .shippingState(ShippingState.READY)
                .orderItemState(OrderItemState.NONE)
                .build();
    }
}
