package com.sem.ecommerce.core.event.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OutBoxQueryRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Mono<Void> updateAllPendingEventsToSent(List<String> ids) {
        String sql = "UPDATE outbox SET state = 'SENT' WHERE state = 'PENDING' AND id IN ANY (:ids)";

        return r2dbcEntityTemplate.getDatabaseClient()
                .sql(sql)
                .bind("ids", ids)
                .fetch()
                .rowsUpdated()
                .then();
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