package com.sem.ecommerce.infra.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sem.ecommerce.domain.order.event.OrderCreatedEvent;
import com.sem.ecommerce.domain.order.port.OrderCreatedEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedEventPublisher implements OrderCreatedEventPublisherPort {
    private static final String OUTPUT_BINDING = "orderProcessor-out-0";
    private static final String EVENT_TYPE = "order.created";

    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> publish(OrderCreatedEvent event) {
        String eventData = null;
        try {
            eventData = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Message<String> message = MessageBuilder
                .withPayload(eventData)
                .setHeader("correlation-id", event.eventId())
                .setHeader("event-type", EVENT_TYPE)
                .build();

        return Mono.fromCallable(() -> streamBridge.send(OUTPUT_BINDING, message))
                .then();
    }
}

