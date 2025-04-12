package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.core.event.outbox.OutBoxQueryRepository;
import com.sem.ecommerce.core.event.outbox.OutboxEvent;
import com.sem.ecommerce.core.mapper.MapperUtils;
import com.sem.ecommerce.domain.order.event.OrderCreatedEvent;
import com.sem.ecommerce.domain.order.port.OrderServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExpiredEventScheduler {
    private final OutBoxQueryRepository outBoxQueryRepository;
    private final OrderServicePort orderServicePort;

    @Scheduled(fixedRateString = "${events.outbox.expired-check-rate:60000}")
    @SchedulerLock(
            name = "expiredEventScheduling",
            lockAtLeastFor = "15s",
            lockAtMostFor = "60s"
    )
    @Transactional
    public void processExpiredEvents() {

        outBoxQueryRepository.findAllExpiredPendingEvents()
                .flatMap(this::processExpiredEvent)
                .doOnError(e -> log.error("Error processing expired event", e))
                .blockLast();
    }

    private Mono<Void> processExpiredEvent(OutboxEvent event) {
        log.info("Processing expired event: {}, eventType: {}", event.getId(), event.getEventKey());

        // 이벤트 타입별 처리 후 이벤트를 Failed로 표시
        Mono<Void> processingMono = switch (event.getEventKey()) {
            case "order.created" -> handleExpiredOrderCreatedEvent(event);
            default -> Mono.empty();
        };

        // 처리 후 모든 이벤트를 Failed로 표시
        return processingMono
                .then(markEventAsFailed(event))
                .onErrorResume(e -> {
                    log.error("Failed to process expired event: {}", event.getId(), e);
                    return markEventAsFailed(event);
                });
    }

    /**
     * 타임아웃된 주문 생성 이벤트를 처리
     */
    private Mono<Void> handleExpiredOrderCreatedEvent(OutboxEvent event) {
        OrderCreatedEvent orderEvent = MapperUtils.fromJson(event.getPayload(), OrderCreatedEvent.class);
        log.info("Handling expired order created event for order: {}", orderEvent.orderId());

        return orderServicePort.handleExpiredOrderCreated(orderEvent.orderId())
                .onErrorResume(e -> {
                    log.error("Error handling expired order: {}", orderEvent.orderId(), e);
                    return Mono.empty();
                });
    }

    private Mono<Void> markEventAsFailed(OutboxEvent event) {
        return outBoxQueryRepository.updateEventStatus(
                event.getId(),
                OutboxEvent.OutboxStatus.FAILED
        );
    }
}