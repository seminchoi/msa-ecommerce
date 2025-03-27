package com.sem.ecommerce.infra.event;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxSchedulerProcessor processor;
    // 캐싱된
    @Scheduled(fixedRate = 500)
    @SchedulerLock(
            name = "longRunningTask",
            lockAtLeastFor = "400ms",
            lockAtMostFor = "1000ms"
    )
    @Transactional
    public Mono<Void> publishOutboxEvents() {
        return processor.processPublish();
    }

    // 캐싱된 acked ID들을 불러옵니다.
    // ack 받은 event들을 SENT 처리합니다.
    // 캐시를 제거합니다.

}
