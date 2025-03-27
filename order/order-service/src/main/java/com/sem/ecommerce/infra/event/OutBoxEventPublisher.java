package com.sem.ecommerce.infra.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sem.ecommerce.domain.order.event.OrderCreatedEvent;
import com.sem.ecormmerce.core.event.repository.OutboxEvent;
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
    private static final String OUTPUT_BINDING = "orderProcessor-out-0";

    private final StreamBridge streamBridge;

    public Mono<Void> publish(OutboxEvent event) {
        Message<String> message = MessageBuilder
                .withPayload(event.getPayload())
                .setHeader("correlation-id", event.getId())
                .setHeader("event-type", event.getEventType())
                .build();

        return Mono.fromCallable(() -> streamBridge.send(OUTPUT_BINDING, message))
                .then();
    }
}

