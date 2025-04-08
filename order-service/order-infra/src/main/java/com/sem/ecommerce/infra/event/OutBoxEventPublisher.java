package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.core.event.repository.OutboxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutBoxEventPublisher {
    //TODO: outbinding 을 ENUM으로 할지, 통합된 채널을 사용할 지 고민해야함
    private static final String OUTPUT_BINDING = "orderProcessor-out-0";

    private final StreamBridge streamBridge;

    public boolean publish(OutboxEvent event) {
        Message<String> message = MessageBuilder
                .withPayload(event.getPayload())
                .setHeader("correlation-id", event.getId())
                .setHeader("event-type", event.getEventType())
                .build();

        return streamBridge.send(OUTPUT_BINDING, message);
    }
}