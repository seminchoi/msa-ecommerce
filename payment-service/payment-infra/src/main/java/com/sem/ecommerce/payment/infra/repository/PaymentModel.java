package com.sem.ecommerce.payment.infra.repository;

import com.sem.ecommerce.payment.domain.Payment;
import com.sem.ecommerce.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table("payments")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentModel implements Persistable<UUID> {
    @Id
    @Column("id")
    private UUID id;

    @Column("order_id")
    private UUID orderId;

    @Column("member_id")
    private UUID memberId;

    @Column("amount")
    private Long amount;

    @Column("status")
    private PaymentStatus status;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Transient
    private boolean isNew;

    public static PaymentModel from(Payment payment) {
        return from(payment, false);
    }

    public static PaymentModel from(Payment payment, boolean isNew) {
        return PaymentModel.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .memberId(payment.getMemberId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .isNew(isNew)
                .build();
    }

    public Payment toDomain() {
        return Payment.builder()
                .id(this.id)
                .orderId(this.orderId)
                .memberId(this.memberId)
                .amount(this.amount)
                .status(this.status)
                .createdAt(this.createdAt)
                .build();
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }
}