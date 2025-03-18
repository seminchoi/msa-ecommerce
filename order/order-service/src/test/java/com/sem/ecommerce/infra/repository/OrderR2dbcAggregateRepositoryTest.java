package com.sem.ecommerce.infra.repository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import com.sem.ecommerce.domain.order.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderR2dbcAggregateRepositoryTest {
    @Autowired
    OrderAggregateR2dbcRepository orderR2dbcAggregateRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.12-alpine");

    @Test
    @DisplayName("유효한 Order 객체는 저장과 조회에 성공한다.")
    void shouldSaveAndFind_whenValidOrder() {
        // given
        Order order = createOrderFixture();

        // when
        orderR2dbcAggregateRepository.save(order).block();

        // then
        verifyOrderSaved(order);
    }

    private void verifyOrderSaved(Order order) {
        Order actual = orderR2dbcAggregateRepository.findById(order.getId()).block();
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(order);
    }


    private Order createOrderFixture() {
        FixtureMonkey sut = buildFixtureMonkey();

        return sut.giveMeBuilder(Order.class)
                .thenApply((it, builder) -> builder.set("orderItems.orderItems[*].orderId", it.getId()))
                .sample();
    }

    private FixtureMonkey buildFixtureMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .plugin(new JakartaValidationPlugin())
                .build();
    }

}
