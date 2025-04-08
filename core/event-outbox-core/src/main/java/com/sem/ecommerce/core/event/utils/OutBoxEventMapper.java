package com.sem.ecommerce.core.event.utils;

import com.sem.ecommerce.core.event.DomainEvent;
import com.sem.ecommerce.core.event.outbox.OutboxEvent;
import com.sem.ecommerce.core.mapper.MapperUtils;

import java.time.ZonedDateTime;

public class OutBoxEventMapper {

    public static OutboxEvent from(DomainEvent event) {
        String payload = MapperUtils.toJson(event);

        return OutboxEvent.builder()
                .id(event.eventId())
                .aggregateId(event.aggregateId())
                .eventType(event.eventType())
                .payload(payload)
                .status(OutboxEvent.OutboxStatus.PENDING)
                .occurredAt(ZonedDateTime.now())
                .build();
    }
}
