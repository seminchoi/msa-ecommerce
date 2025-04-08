package com.sem.ecommerce.core.event.outbox;

import com.sem.ecommerce.core.event.EventType;
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
     * ACK 는 RabbitPolish 클래스에서 처리합니다.
     * 만약 캐시가 유실되어 재전송하게 되더라도 Consumer 에서 멱등성을 보장하기 때문에 크게 문제되지 않습니다.
     * DB 에 저장된 최근 이벤트 중 전송되지 않은 이벤트를 publish 합니다.
     */
    public <T extends Enum<T> & EventType> Mono<Void> publish(Class<T> enumClass) {
        return updateSentEvents()
                .then(publishEvents(enumClass));
    }


    private Mono<Void> updateSentEvents() {
        return outBoxCache.getAllAckedOutboxEvents()
                .collectList()
                .flatMap(it -> outBoxQueryRepository.updateAllPendingEventsToSent(it)
                        .then(outBoxCache.removeAckedOutboxEvent(it)));
    }

    private <T extends Enum<T> & EventType> Mono<Void> publishEvents(Class<T> enumClass) {
        return outBoxQueryRepository.findAllCurrentPendingEvents(10)
                .map(event -> publishEvent(event, enumClass))
                .then();
    }

    private <T extends Enum<T> & EventType> boolean publishEvent(OutboxEvent event, Class<T> enumClass) {
        T eventType = EventType.fromEventKey(event.getEventType(), enumClass);

        Message<String> message = MessageBuilder
                .withPayload(event.getPayload())
                .setHeader("correlation-id", event.getId())
                .setHeader("event-type", event.getEventType())
                .build();

        return streamBridge.send(eventType.getOutputBinding(), message);
    }
}