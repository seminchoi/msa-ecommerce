package com.sem.ecormmerce.core.event.utils;

import com.sem.ecormmerce.core.event.DomainEvent;
import com.sem.ecormmerce.core.event.repository.OutboxEvent;
import com.sem.ecormmerce.core.mapper.MapperUtils;

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
