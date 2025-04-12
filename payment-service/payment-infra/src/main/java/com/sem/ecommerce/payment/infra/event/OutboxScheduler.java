package com.sem.ecommerce.payment.infra.event;

import com.sem.ecommerce.core.event.outbox.OutBoxEventPublisher;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutBoxEventPublisher outBoxEventPublisher;

    @Scheduled(fixedRateString = "${events.outbox.publish-rate}")
    @SchedulerLock(
            name = "outboxScheduling",
            lockAtLeastFor = "${events.outbox.least-lock-time}",
            lockAtMostFor = "${events.outbox.most-lock-time}"
    )
    @Transactional
    public void publishOutboxEvents() {
        outBoxEventPublisher.publish() // enumClass 인자 제거됨
                .block();
    }
}