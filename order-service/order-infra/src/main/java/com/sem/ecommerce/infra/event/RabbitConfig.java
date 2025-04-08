package com.sem.ecommerce.infra.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Slf4j
public class RabbitConfig {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @PostConstruct
    public void init() {
        createExchanges();
    }

    private void createExchanges() {
        Exchange exchange = new TopicExchange("orders", true, false);
        amqpAdmin.declareExchange(exchange);
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