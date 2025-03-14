package com.sem.ecommerce.infra.repository.model;

import com.sem.ecommerce.domain.order.Order;
import com.sem.ecommerce.domain.order.OrderItem;
import com.sem.ecommerce.domain.order.OrderItemState;
import com.sem.ecommerce.domain.order.ShippingState;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Table("order_item")
@Builder
public record OrderItemModel(
        @Id
        UUID id,

        @Column("order_id")
        UUID orderId,

        @Column("catalog_id")
        UUID catalogId,

        @Column("unit_price")
        Long unitPrice,

        @Column("quantity")
        Integer quantity,

        @Column("order_item_state")
        OrderItemState orderItemState,

        @Column("shipping_state")
        ShippingState shippingState
) {


    public OrderItem toDomain() {
        return OrderItem.builder()
                .id(id)
                .catalogId(catalogId)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .shippingState(shippingState)
                .orderItemState(orderItemState)
                .build();
    }
}