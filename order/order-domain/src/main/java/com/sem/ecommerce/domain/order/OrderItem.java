package com.sem.ecommerce.domain.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

// 배송 중, 배송 완료 여부와 상관없이 환불, 교환 될 수 있습니다.
// 환불, 교환 테이블과 JOIN 해서 환불, 교환 상태를 알 수 있지만 항상 JOIN 하기에는 오버헤드가 생길 수 있다.
// READ 모델과 WRITE 모델을 분리하면 구조를 좀 더 변경할 수 있다.
@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class OrderItem {
    @NotNull
    private UUID id;

    @NotNull
    private UUID catalogId;

    @Min(1)
    private long unitPrice;

    @Min(1)
    private int quantity;

    @NotNull
    private ShippingState shippingState;

    @NotNull
    private OrderItemState orderItemState;
}
