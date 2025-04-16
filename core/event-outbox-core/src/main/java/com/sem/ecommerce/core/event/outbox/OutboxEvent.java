package com.sem.ecommerce.core.event.outbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "outbox_event")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent implements Persistable<UUID> {
    @Id
    private UUID id;

    private String aggregateId;

    // EventType 관련 필드들
    private String eventKey;
    private String outputBinding;
    private int expirationMinutes;

    private String payload;
    private ZonedDateTime occurredAt;
    private ZonedDateTime expiresAt;
    private OutboxStatus status;

    @Transient
    private boolean isNew;

    public enum OutboxStatus {
        PENDING, SENT, FAILED
    }

    // 만료 여부 확인 메서드
    public boolean isExpired() {
        // expiresAt이 null이면 만료되지 않음
        if (expiresAt == null) {
            return false;
        }
        return ZonedDateTime.now().isAfter(expiresAt);
    }
}