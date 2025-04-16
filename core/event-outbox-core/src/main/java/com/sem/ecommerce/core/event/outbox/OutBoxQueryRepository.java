package com.sem.ecommerce.core.event.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OutBoxQueryRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Mono<Void> updateAllPendingEventsToSent(List<String> ids) {
        if (ids.isEmpty()) {
            return Mono.empty();
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            placeholders.append(i > 0 ? ", " : "").append(":id").append(i);
        }

        String sql = String.format(
                "UPDATE outbox_event SET status = 'SENT' WHERE status = 'PENDING' AND id IN (%s)",
                placeholders
        );

        var executeSpec = r2dbcEntityTemplate.getDatabaseClient().sql(sql);

        // 각 ID를 개별적으로 바인딩
        for (int i = 0; i < ids.size(); i++) {
            final int index = i;
            executeSpec = executeSpec.bind("id" + index, UUID.fromString(ids.get(index)));
        }

        return executeSpec.fetch().rowsUpdated().then();
    }

    public Flux<OutboxEvent> findAllCurrentPendingEvents() {
        ZonedDateTime now = ZonedDateTime.now();

        return r2dbcEntityTemplate.select(OutboxEvent.class)
                .matching(Query.query(
                        Criteria.where("status").is(OutboxEvent.OutboxStatus.PENDING)
                                .and(
                                        Criteria.where("expires_at").isNull()
                                                .or(Criteria.where("expires_at").greaterThan(now))
                                )
                ))
                .all();
    }

    /**
     * 만료되었지만 아직 처리되지 않은 이벤트 조회
     */
    public Flux<OutboxEvent> findAllExpiredPendingEvents() {
        ZonedDateTime now = ZonedDateTime.now();

        return r2dbcEntityTemplate.select(OutboxEvent.class)
                .matching(Query.query(
                        Criteria.where("status").is(OutboxEvent.OutboxStatus.PENDING)
                                .and(Criteria.where("expires_at").lessThan(now))
                                .and(Criteria.where("expires_at").isNotNull())
                ))
                .all();
    }

    /**
     * 이벤트 상태 업데이트
     */
    public Mono<Void> updateEventStatus(UUID id, OutboxEvent.OutboxStatus status) {
        return r2dbcEntityTemplate.update(OutboxEvent.class)
                .matching(Query.query(Criteria.where("id").is(id)))
                .apply(Update.update("status", status))
                .then();
    }
}