package com.sem.ecommerce.core.event.utils;

import com.sem.ecommerce.core.event.DomainEvent;
import com.sem.ecommerce.core.event.outbox.EventType;
import com.sem.ecommerce.core.event.outbox.EventTypeHolder;
import com.sem.ecommerce.core.event.outbox.OutboxEvent;
import com.sem.ecommerce.core.mapper.MapperUtils;

import java.time.ZonedDateTime;

public class OutBoxEventMapper {

    private OutBoxEventMapper() {
        // Utility class, private constructor
    }

    public static OutboxEvent from(DomainEvent event) {
        String payload = MapperUtils.toJson(event);
        ZonedDateTime occurredAt = ZonedDateTime.now();

        String eventKey = event.eventType();

        EventType eventType = EventTypeHolder.getEventType(eventKey);
        String outputBinding = eventType.getOutputBinding();
        int expirationMinutes = eventType.getExpirationTimeInMinutes();

        // 만료 시간 계산
        ZonedDateTime expiresAt = null;
        if (expirationMinutes > 0) {
            expiresAt = occurredAt.plusMinutes(expirationMinutes);
        }

        return OutboxEvent.builder()
                .id(event.eventId())
                .aggregateId(event.aggregateId())
                .eventKey(eventKey)
                .outputBinding(outputBinding)
                .expirationMinutes(expirationMinutes)
                .payload(payload)
                .status(OutboxEvent.OutboxStatus.PENDING)
                .occurredAt(occurredAt)
                .expiresAt(expiresAt)
                .isNew(true)
                .build();
    }
}