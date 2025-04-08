package com.sem.ecommerce.infra.repository.model;

import com.sem.ecommerce.domain.order.OrderItem;
import com.sem.ecommerce.domain.order.OrderItemState;
import com.sem.ecommerce.domain.order.ShippingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("order_item")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemModel implements Persistable<UUID> {

    @Id
    @Column("id")
    private UUID id;

    @Column("order_id")
    private UUID orderId;

    @Column("catalog_id")
    private UUID catalogId;

    @Column("unit_price")
    private Long unitPrice;

    @Column("quantity")
    private Integer quantity;

    @Column("order_item_state")
    private OrderItemState orderItemState;

    @Column("shipping_state")
    private ShippingState shippingState;

    @Transient
    private boolean isNew;

    public static OrderItemModel from(UUID orderId, OrderItem orderItem) {
        return from(orderId, orderItem, false);
    }

    public static OrderItemModel from(UUID orderId, OrderItem orderItem, boolean isNew) {
        return OrderItemModel.builder()
                .id(orderItem.getId())
                .orderId(orderId)
                .catalogId(orderItem.getCatalogId())
                .unitPrice(orderItem.getUnitPrice())
                .quantity(orderItem.getQuantity())
                .orderItemState(orderItem.getOrderItemState())
                .shippingState(orderItem.getShippingState())
                .isNew(isNew)
                .build();
    }

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
