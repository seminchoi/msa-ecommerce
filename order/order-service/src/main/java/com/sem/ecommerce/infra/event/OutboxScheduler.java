package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.infra.cache.OutBoxCache;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutBoxQueryRepository outBoxQueryRepository;
    private final OutBoxCache outBoxCache;
    private final OutBoxEventPublisher outBoxEventPublisher;


    @Scheduled(fixedRateString = "${events.outbox.publish-rate}")
    @SchedulerLock(
            name = "longRunningTask",
            lockAtLeastFor = "${events.outbox.least-lock-time}",
            lockAtMostFor = "${events.outbox.most-lock-time}"
    )
    @Transactional
    public void publishOutboxEvents() {
        updateSentEvents()
                .then(publishEvents())
                .block();
    }

    // Publish Confirm을 통해 ACK를 받고 캐싱한 이벤트들을 DB에 SENT상태로 업데이트 합니다.
    // ACK를 받아서 캐싱하는 작업은 RedisPolicy 클래스에서 처리합니다.
    private Mono<Void> updateSentEvents() {
        return outBoxCache.getAllAckedOutboxEvents()
                .collectList()
                .flatMap(it -> outBoxQueryRepository.updateAllPendingEventsToSent(it)
                        .then(outBoxCache.removeAckedOutboxEvent(it)));
    }

    // Pending 상태의 최근 이벤트들을 Publish 합니다.
    // 오래된 Pending 상태의 이벤트들은 Publish하지 않습니다.
    private Mono<Void> publishEvents() {
        return outBoxQueryRepository.findAllCurrentPendingEvents(10)
                .map(outBoxEventPublisher::publish)
                .then();
    }
}
