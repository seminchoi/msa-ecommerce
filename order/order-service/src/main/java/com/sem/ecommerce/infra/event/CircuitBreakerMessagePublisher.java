package com.sem.ecommerce.infra.event;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CircuitBreakerMessagePublisher {
    private final StreamBridge streamBridge;

    @CircuitBreaker(name = "messaging-service", fallbackMethod = "handleFailure")
    public boolean send(String bindingName, Message<?> message) {
        return streamBridge.send(bindingName, message);
    }

    public boolean handleFailure(Message<?> message, Exception exception) {
        log.error("""
            Failed to send message
            message id : {}
            cause: {}: {}
        """, message.getPayload(), exception, exception.getMessage());
        return false;
    }
}
