package com.sem.ecommerce.infra.cache;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class OutBoxCache {
    private static final String KEY = "order:events:acked";
    private final ReactiveSetOperations<String, String> setOps;

    public OutBoxCache(ReactiveRedisTemplate<String, String> reactiveRedisStringTemplate) {
        this.setOps = reactiveRedisStringTemplate.opsForSet();
    }

    public Mono<Void> addAckedOutboxEvent(String id) {
        return setOps.add(KEY, id).then();
    }

    public Flux<String> getAllAckedOutboxEvents() {
        return setOps.members(KEY);
    }

    public Mono<Void> removeAckedOutboxEvent(List<String> ids) {
        return setOps.remove(KEY, ids).then();
    }
}
