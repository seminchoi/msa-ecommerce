package com.sem.ecommerce.infra.event;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitConfig {
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public static final String QUEUE_NAME = "my-service-queue";

    @Bean
    public Queue queue() {
        // durable을 true로 설정하여 RabbitMQ 재시작에도 큐가 유지되도록 함
        return new Queue(QUEUE_NAME, true, false, false);
    }

//    @Bean
//    public SenderOptions senderOptions(Mono<Connection> connectionMono) {
//        return new SenderOptions()
//                .connectionMono(connectionMono)
//                .resourceManagementScheduler(Schedulers.boundedElastic());
//    }
//
//    @Bean
//    public Sender sender(SenderOptions senderOptions) {
//        return RabbitFlux.createSender(senderOptions);
//    }

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
                    // 여기서 재시도 로직을 구현할 수 있음
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