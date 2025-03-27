package com.sem.ecommerce.infra;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.ReactiveRedisLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    @Bean
    public LockProvider lockProvider(ReactiveRedisConnectionFactory connectionFactory) {
        return new ReactiveRedisLockProvider.Builder(connectionFactory)
                .build();
    }
}
