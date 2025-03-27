package com.sem.ecormmerce.core.event.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sem.ecormmerce.core.event.DomainEvent;
import com.sem.ecormmerce.core.event.repository.OutboxEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class OutBoxEventMapper {
    private static ObjectMapper objectMapper;

    @Autowired
    private ObjectMapper injectedObjectMapper;

    @PostConstruct
    public void init() {
        objectMapper = injectedObjectMapper;
    }

    public static OutboxEvent from(DomainEvent event) {
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

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
