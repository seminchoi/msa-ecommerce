package com.sem.ecommerce.infra;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RabbitAutoConfiguration.class
})
@Slf4j
public class RabbitBasicTest {
    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management-alpine");

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("rabbitmq connection 생성 및 메시지 전송이 수행이 성공한다.")
    void shouldSuccessConnect() throws InterruptedException {
        assertTrue(rabbitMQContainer.isRunning());
        assertNotNull(connectionFactory);

        // 실제 연결 테스트
        assertTrue(connectionFactory.createConnection().isOpen());

        // 간단한 메시지 전송 테스트
        rabbitTemplate.convertAndSend("orders", "test-routing-key", "Hello RabbitMQ", new CorrelationData(UUID.randomUUID().toString()));
    }
}
