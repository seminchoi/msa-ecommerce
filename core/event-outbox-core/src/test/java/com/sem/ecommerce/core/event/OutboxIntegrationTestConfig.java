package com.sem.ecommerce.core.event;

import com.redis.testcontainers.RedisContainer;
import com.sem.ecommerce.core.event.outbox.EventTypeHolder;
import com.sem.ecommerce.core.r2dbc.converter.LocalToZonedDateTimeConverter;
import com.sem.ecommerce.core.r2dbc.converter.ZoneToLocalDateTimeConverter;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.H2Dialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.org.apache.commons.lang3.Conversion;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
@EnableR2dbcRepositories
public class OutboxIntegrationTestConfig {

    @Container
    @ServiceConnection
    public static final RabbitMQContainer RABBIT_MQ_CONTAINER =
            new RabbitMQContainer("rabbitmq:3.8-management-alpine");

    @Container
    @ServiceConnection
    public static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));

    static {
        RABBIT_MQ_CONTAINER.start();
        REDIS_CONTAINER.start();
    }

    @Bean
    public EventTypeRegistrar eventTypeRegistrar() {
        return new EventTypeRegistrar();
    }

    public static class EventTypeRegistrar {
        @PostConstruct
        public void registerEventTypes() {
            EventTypeHolder.registerEventTypes(TestEventType.class);
        }
    }

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new LocalToZonedDateTimeConverter());
        converters.add(new ZoneToLocalDateTimeConverter());
        return R2dbcCustomConversions.of(H2Dialect.INSTANCE, converters);
    }
}