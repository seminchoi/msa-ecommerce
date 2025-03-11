package com.sem.ecommerce.domain.refund;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class Refund {
    private UUID id;
    private UUID orderItemId;
    private int requestAmount;
    private int completedAmount;
    private RefundRejectionReason rejectionReason;
    private RefundState refundState;

    @Builder
    public Refund(UUID id, UUID orderItemId, int requestAmount, int completedAmount, RefundRejectionReason rejectionReason, RefundState refundState) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.requestAmount = requestAmount;
        this.completedAmount = completedAmount;
        this.rejectionReason = rejectionReason;
        this.refundState = refundState;
    }
}
