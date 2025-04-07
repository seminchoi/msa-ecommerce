package com.sem.ecommerce.payment.infra.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Autowired
    private AmqpAdmin amqpAdmin;

    private static final String ORDERS_EXCHANGE = "orders";
    private static final String ORDER_CREATED_QUEUE = "order.created.queue";
    private static final String ORDER_CREATED_DLX = "order.created.dlx";
    private static final String ORDER_CREATED_DLQ = "order.created.dlq";

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @PostConstruct
    public void init() {
        createExchanges();
        createQueues();
    }

    private void createExchanges() {
        // 데드 레터 교환기 생성
        Exchange dlx = new DirectExchange(ORDER_CREATED_DLX, true, false);
        amqpAdmin.declareExchange(dlx);
    }

    private void createQueues() {
        // 메인 큐 생성 - 타임아웃 및 데드 레터 교환기 설정
        Queue orderQueue = QueueBuilder.durable(ORDER_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_CREATED_DLX)
                .withArgument("x-dead-letter-routing-key", "order.created")
                .withArgument("x-message-ttl", 30000) // 30초 타임아웃
                .build();
        amqpAdmin.declareQueue(orderQueue);

        // 메인 큐와 교환기 바인딩
        Binding binding = BindingBuilder.bind(orderQueue)
                .to(new TopicExchange(ORDERS_EXCHANGE))
                .with("order.created");
        amqpAdmin.declareBinding(binding);

        // 데드 레터 큐 생성
        Queue dlq = QueueBuilder.durable(ORDER_CREATED_DLQ)
                .build();
        amqpAdmin.declareQueue(dlq);

        // 데드 레터 큐와 DLX 바인딩
        Binding dlqBinding = BindingBuilder.bind(dlq)
                .to(new DirectExchange(ORDER_CREATED_DLX))
                .with("order.created");
        amqpAdmin.declareBinding(dlqBinding);
    }
}