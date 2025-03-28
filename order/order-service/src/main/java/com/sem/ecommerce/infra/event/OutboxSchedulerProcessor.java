package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.infra.cache.OutBoxCache;
import com.sem.ecormmerce.core.event.repository.OutboxEvent;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
public class OutboxSchedulerProcessor {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final OutBoxCache outBoxCache;
    private final OutBoxEventPublisher outBoxEventPublisher;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public Mono<Void> processPublish() {
        CircuitBreaker messagingService = circuitBreakerRegistry.circuitBreaker("messaging-service");
        CircuitBreaker.State state = messagingService.getEventPublisher().onStateTransition(
                event -> event.getStateTransition()
        );

        return switch (state) {
            case OPEN -> processMessagingFallback();
            default -> processMessagingHealthy();
        };
    }

    public Mono<Void> processMessagingHealthy() {
        return updateSentEvents()
                .then(publishEvents());
    }

    public Mono<Void> processMessagingFallback() {
        CircuitBreaker messagingService = circuitBreakerRegistry.circuitBreaker("messaging-service");
        CircuitBreaker.State state = messagingService.getState();

        // 장애 복구 테스트 상황
        if(!curHealthState.get() && state.equals(CircuitBreaker.State.HALF_OPEN)) {

        }

        // 장애 완전 복구 상황
        if(!curHealthState.get() && state.equals(CircuitBreaker.State.CLOSED) {
            curHealthState.set(true);
        }

        // 장애 발생 상황
        if(curHealthState.get() && state.equals(CircuitBreaker.State.OPEN)) {
            curHealthState.set(false);
        }


        // 장애 발생 상황
        if()
    }

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
                .map(outBoxEventPublisher::publish)
                .then();
    }
}
