package com.sem.ecommerce;

import com.sem.ecommerce.payment.domain.PaymentServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
class PaymentInfraTest {
    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management-alpine");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.12-alpine");

    @MockitoBean
    PaymentServicePort paymentService;

    @Test
    void contextLoads() {
    }

}
