package com.sem.ecommerce.infra.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@Slf4j
public class RabbitConfig {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String QUEUE_NAME = "my-service-queue";

    @Bean
    public Queue queue() {
        // durable을 true로 설정하여 RabbitMQ 재시작에도 큐가 유지되도록 함
        return new Queue(QUEUE_NAME, true, false, false);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @PostConstruct
    public void init() {
        // 메시지 확인 콜백 설정 - 브로커에게 메시지가 전달되었는지 확인
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (correlationData != null) {
                if (ack) {
                    log.info("Message with correlation ID: {} was acknowledged by broker",
                            correlationData.getId());
                } else {
                    log.error("Message with correlation ID: {} was not acknowledged by broker. Cause: {}",
                            correlationData.getId(), cause);
                    String messageId = correlationData.getId();
                    Object originalMessage = correlationData.getReturned() != null ?
                            correlationData.getReturned().getMessage() : null;
                    String exchangeName = correlationData.getReturned() != null ?
                            correlationData.getReturned().getExchange() : "defaultExchange";
                    String routingKey = correlationData.getReturned() != null ?
                            correlationData.getReturned().getRoutingKey() : "defaultRoutingKey";

                    retryMessagePublish(messageId, originalMessage, exchangeName, routingKey);
                }
            }
        });

        // 반환된 메시지 콜백 설정 - 라우팅할 수 없는 메시지 처리
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("Message returned from broker: {} with routing key: {} to exchange: {}. Reply: {}",
                    returned.getMessage(), returned.getRoutingKey(),
                    returned.getExchange(), returned.getReplyText());
            // 여기서 반환된 메시지를 처리할 수 있음
        });
    }


    private void retryMessagePublish(String messageId, Object message, String exchange, String routingKey) {
        int maxRetries = 3;
        long initialBackoff = 1000; // 1초

        RetryTemplate retryTemplate = new RetryTemplate();

        // 지수 백오프 정책 설정
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(initialBackoff);
        backOffPolicy.setMultiplier(2.0); // 재시도마다 대기 시간 2배 증가
        backOffPolicy.setMaxInterval(10000); // 최대 10초
        retryTemplate.setBackOffPolicy(backOffPolicy);

        // 재시도 정책 설정
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxRetries);
        retryTemplate.setRetryPolicy(retryPolicy);

        try {
            retryTemplate.execute(context -> {
                log.info("Retrying message publish for message ID: {}. Attempt: {}",
                        messageId, context.getRetryCount() + 1);

                // 새로운 CorrelationData 생성
                CorrelationData newCorrelationData = new CorrelationData(messageId);

                // 메시지 재발행
                rabbitTemplate.convertAndSend(exchange, routingKey, message, newCorrelationData);
                return null;
            });
        } catch (Exception e) {
            log.error("Failed to publish message after {} retries. Message ID: {}, Error: {}",
                    maxRetries, messageId, e.getMessage());
            // 최종 재시도 실패 후 처리 (DB에 저장하거나 알림 발송 등)
        }
    }

//    @Bean
//    public RetryTemplate orderCreatedRetryTemplate() {
//        RetryTemplate retryTemplate = new RetryTemplate();
//
//        // 최대 3번 재시도 설정
//        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
//        retryPolicy.setMaxAttempts(3);
//        retryTemplate.setRetryPolicy(retryPolicy);
//
//        // 백오프 정책 (재시도 간격) 설정
//        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
//        backOffPolicy.setInitialInterval(1000); // 초기 1초
//        backOffPolicy.setMultiplier(2.0);       // 매 재시도마다 2배로 증가
//        backOffPolicy.setMaxInterval(3000);     // 최대 3초까지만
//        retryTemplate.setBackOffPolicy(backOffPolicy);
//
//        return retryTemplate;
//    }
}