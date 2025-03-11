package com.sem.ecommerce.domain.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

// 배송 중, 배송 완료 여부와 상관없이 환불, 교환 될 수 있습니다.
// 환불, 교환 테이블과 JOIN 해서 환불, 교환 상태를 알 수 있지만 항상 JOIN 하기에는 오버헤드가 생길 수 있다.
// READ 모델과 WRITE 모델을 분리하면 구조를 좀 더 변경할 수 있다.
@Getter
@EqualsAndHashCode(of = "id")
public class OrderItem {
    private UUID id;
    private UUID productId;
    private long unitPrice;
    private int quantity;
    private ShippingState shippingState;
    private OrderItemState orderItemState;

    public OrderItem(UUID id, UUID productId, long unitPrice, int quantity, ShippingState shippingState, OrderItemState orderItemState) {
        this.id = id;
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.shippingState = shippingState;
        this.orderItemState = orderItemState;
    }
}
