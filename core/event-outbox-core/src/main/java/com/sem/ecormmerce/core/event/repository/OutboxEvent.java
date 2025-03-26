package com.sem.ecormmerce.core.event.repository;

import com.sem.ecormmerce.core.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "outbox_event")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {
    @Id
    private UUID id;

    private String aggregateType;

    private String aggregateId;

    private String eventType;

    private String payload;

    private ZonedDateTime processedAt;

    private OutboxStatus status;

    public enum OutboxStatus {
        PENDING, SENT, FAILED
    }
}