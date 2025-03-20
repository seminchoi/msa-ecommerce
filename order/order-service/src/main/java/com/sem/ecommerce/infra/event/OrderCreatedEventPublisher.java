package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.domain.order.event.OrderCreatedEvent;
import com.sem.ecommerce.domain.order.port.OrderCreatedEventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class OrderCreatedEventPublisher implements OrderCreatedEventPublisherPort {
    private static final String OUTPUT_BINDING = "orderProcessor-out-0";
    private static final String EVENT_TYPE = "order.created";

    private final StreamBridge streamBridge;


    public OrderCreatedEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public Mono<Void> publish(OrderCreatedEvent event) {
        Message<OrderCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader("event-type", EVENT_TYPE)
                .build();

        return Mono.fromCallable(() -> streamBridge.send(OUTPUT_BINDING, message))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100))
                        .filter(throwable -> throwable instanceof MessageDeliveryException)
                        .doBeforeRetry(retrySignal ->
                                log.warn("Retrying message delivery. Attempt: {}", retrySignal.totalRetries() + 1))
                )
                .doOnError(ex -> log.error("Failed to publish OrderCreatedEvent after {} retries: {}",
                        3, ex.getMessage()))
                .then();
    }
}