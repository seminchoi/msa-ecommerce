package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.core.event.repository.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

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

    public Flux<OutboxEvent> findAllCurrentPendingEvents(long period) {
        ZonedDateTime startTime = ZonedDateTime.now().minusMinutes(period);

        return r2dbcEntityTemplate.select(OutboxEvent.class)
                .matching(Query.query(
                        Criteria.where("occurred_at").greaterThanOrEquals(startTime)
                ))
                .all();
    }
}
