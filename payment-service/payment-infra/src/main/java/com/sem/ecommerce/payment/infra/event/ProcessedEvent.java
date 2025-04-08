package com.sem.ecommerce.payment.infra.event;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table("processed_event")
@Getter
public class ProcessedEvent {
    @Id
    private UUID id;
    private ZonedDateTime processedAt = ZonedDateTime.now();

    public ProcessedEvent(UUID id) {
        this.id = id;
    }
}
