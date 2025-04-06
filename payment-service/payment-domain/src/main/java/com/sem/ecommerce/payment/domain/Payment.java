package com.sem.ecommerce.payment.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class Payment {
    private UUID id;
    private UUID orderId;
    private UUID customerId;
    private long amount;
    private PaymentStatus status;
    private ZonedDateTime createdAt;

    @Builder
    public Payment(UUID id, UUID orderId, UUID customerId, long amount, PaymentStatus status, ZonedDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }
}