package com.sem.ecommerce.domain.order;

import com.sem.ecormmerce.core.event.DomainEventArchive;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@Builder
public class Order {
    @NotNull
    private UUID id;

    @NotNull
    private UUID ordererId;

    @Valid
    @NotNull
    private Receiver receiver;

    @NotNull
    private OrderState orderState;

    @Valid
    @NotNull
    @Delegate
    private OrderItems orderItems;

    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Builder.Default
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @Delegate
    @Builder.Default
    private DomainEventArchive archive = new DomainEventArchive();
}
