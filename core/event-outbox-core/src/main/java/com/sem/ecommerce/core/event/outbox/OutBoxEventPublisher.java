package com.sem.ecommerce.core.event.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutBoxEventPublisher {
    private final OutBoxQueryRepository outBoxQueryRepository;
    private final OutBoxCache outBoxCache;
    private final StreamBridge streamBridge;

    /**
     * ACK 를 받아 캐싱된 이벤트들을 DB에 SENT 처리 합니다.
     * DB 에 저장된 만료되지 않은 이벤트 중 전송되지 않은 이벤트를 publish 합니다.
     */
    public Mono<Void> publish() {
        return updateSentEvents()
                .then(publishEvents());
    }

    private Mono<Void> updateSentEvents() {
        return outBoxCache.getAllAckedOutboxEvents()
                .collectList()
                .flatMap(it -> outBoxQueryRepository.updateAllPendingEventsToSent(it)
                        .then(outBoxCache.removeAckedOutboxEvent(it)));
    }

    private Mono<Void> publishEvents() {
        return outBoxQueryRepository.findAllCurrentPendingEvents()
                .map(this::publishEvent)
                .then();
    }

    private boolean publishEvent(OutboxEvent event) {
        // 이제 event에서 직접 outputBinding을 가져옴
        String outputBinding = event.getOutputBinding();

        Message<String> message = MessageBuilder
                .withPayload(event.getPayload())
                .setHeader("correlation-id", event.getId())
                .setHeader("event-type", event.getEventKey())
                .build();

        log.info("Publishing event to binding: {}, eventType: {}", outputBinding, event.getEventKey());
        return streamBridge.send(outputBinding, message);
    }
}