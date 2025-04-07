package com.sem.ecommerce.payment.domain;

import com.sem.ecormmerce.core.event.DomainEventArchive;
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
    private long amount;
    private PaymentStatus status;
    private ZonedDateTime createdAt;
    @Delegate
    @Builder.Default
    private DomainEventArchive archive = new DomainEventArchive();

}