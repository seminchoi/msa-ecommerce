package com.sem.ecommerce.domain.order;

import com.sem.ecormmerce.core.event.DomainEvent;
import com.sem.ecormmerce.core.event.DomainEventArchive;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
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
    private OrderItems orderItems;

    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Builder.Default
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @Delegate
    @Builder.Default
    private DomainEventArchive archive = new DomainEventArchive();
}
