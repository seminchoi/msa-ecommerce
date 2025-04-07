package com.sem.ecommerce.payment.infra.repository;

import com.sem.ecommerce.payment.domain.PaymentMethod;
import com.sem.ecommerce.payment.domain.PaymentMethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("payment_methods")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodModel implements Persistable<UUID> {
    @Id
    @Column("id")
    private UUID id;

    @Column("member_id")
    private String memberId;

    @Column("type")
    private PaymentMethodType type;

    @Column("masked_number")
    private String maskedNumber;

    @Column("issuer")
    private String issuer;

    @Column("is_default")
    private Boolean isDefault;

    @Column("registered_at")
    private LocalDateTime registeredAt;

    @Transient
    private boolean isNew;

    public static PaymentMethodModel from(PaymentMethod paymentMethod) {
        return from(paymentMethod, false);
    }

    public static PaymentMethodModel from(PaymentMethod paymentMethod, boolean isNew) {
        return PaymentMethodModel.builder()
                .id(paymentMethod.getId())
                .memberId(paymentMethod.getMemberId())
                .type(paymentMethod.getType())
                .maskedNumber(paymentMethod.getMaskedNumber())
                .issuer(paymentMethod.getIssuer())
                .isDefault(paymentMethod.isDefault())
                .registeredAt(paymentMethod.getRegisteredAt())
                .isNew(isNew)
                .build();
    }

    public PaymentMethod toDomain() {
        return PaymentMethod.builder()
                .id(this.id)
                .memberId(this.memberId)
                .type(this.type)
                .maskedNumber(this.maskedNumber)
                .issuer(this.issuer)
                .isDefault(this.isDefault)
                .registeredAt(this.registeredAt)
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