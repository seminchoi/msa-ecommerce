package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.infra.cache.OutBoxCache;
import com.sem.ecormmerce.core.event.repository.OutboxEvent;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final OutBoxCache outBoxCache;
    private final OutBoxEventPublisher outBoxEventPublisher;

    // 캐싱된
    @Scheduled(fixedRate = 500)
    @SchedulerLock(
            name = "longRunningTask",
            lockAtLeastFor = "400ms",
            lockAtMostFor = "1000ms"
    )
    @Transactional
    public Mono<Void> publishOutboxEvents() {
        return updateSentEvents()
                .then(publishEvents());
    }

    // 캐싱된 acked ID들을 불러옵니다.
    // ack 받은 event들을 SENT 처리합니다.
    // 캐시를 제거합니다.
    private Mono<Void> updateSentEvents() {
        return outBoxCache.getAllAckedOutboxEvents()
                .collectList()
                .flatMap(it -> executeUpdate(it)
                        .then(outBoxCache.removeAckedOutboxEvent(it)));
    }

    private Mono<Void> executeUpdate(List<String> ids) {
        String sql = "UPDATE outbox SET state = 'SENT' WHERE state = 'PENDING' AND id IN ANY (:ids)";

        return r2dbcEntityTemplate.getDatabaseClient()
                .sql(sql)
                .bind("ids", ids)
                .fetch()
                .rowsUpdated()
                .then();
    }

    private Mono<Void> publishEvents() {
        ZonedDateTime startTime = ZonedDateTime.now().minusMinutes(10);

        return r2dbcEntityTemplate.select(OutboxEvent.class)
                .matching(Query.query(
                        Criteria.where("occurred_at").greaterThanOrEquals(startTime)
                ))
                .all()
                .flatMap(outBoxEventPublisher::publish)
                .then();
    }
}
