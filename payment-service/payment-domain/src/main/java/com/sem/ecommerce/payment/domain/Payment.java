package com.sem.ecommerce.payment.domain;

import com.sem.ecommerce.core.event.DomainEventArchive;
import com.sem.ecommerce.payment.domain.event.PaymentCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Payment {
    private UUID id;
    private UUID orderId;
    private UUID memberId;
    private UUID paymentMethodId;
    private long amount;
    private PaymentStatus status;
    private ZonedDateTime createdAt;

    @Delegate
    @Builder.Default
    private DomainEventArchive archive = new DomainEventArchive();

    public static Payment create(
            UUID orderId,
            UUID memberId,
            UUID paymentMethodId,
            long amount
    ) {
        UUID paymentId = UUID.randomUUID();
        Payment payment = Payment.builder()
                .id(paymentId)
                .orderId(orderId)
                .memberId(memberId)
                .paymentMethodId(paymentMethodId)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .createdAt(ZonedDateTime.now())
                .build();

        PaymentCreatedEvent event = PaymentCreatedEvent.from(payment);
        payment.register(event);

        return payment;
    }
}