package com.sem.ecommerce.infra;

import com.redis.testcontainers.RedisContainer;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@Slf4j
@ContextConfiguration(classes = {
        SchedulingConfig.class,
        ShedLockConfig.class,
        RedisAutoConfiguration.class,
        ShedLockSchedulerTest.TestScheduler.class
})
public class ShedLockSchedulerTest {
    @Container
    @ServiceConnection
    protected static final RedisContainer REDIS_CONTAINER = new RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG)
    );

    @Test
    @DisplayName("Reactive Scheduler를 Shed Lock과 함께 실행하면 구독과 락이 잘 작동한다.")
    void shouldSubscribeAndLock_whenReactiveSchedulerWithShedLock() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);

        Assertions.assertThat(TestScheduler.executedCount).isEqualTo(1);
    }

    @Component
    static class TestScheduler {
        public static volatile int executedCount = 0;

        @Scheduled(initialDelay = 0, fixedDelay = 100)
        @SchedulerLock(
                name = "test-schedule-lock",
                lockAtLeastFor = "100ms",
                lockAtMostFor = "10s"
        )
        public void schedule() {
            Mono.fromCallable(() -> {
                        executedCount+=1;
                        Thread.sleep(1100);
                        return null;
                    })
                    .subscribeOn(Schedulers.boundedElastic())
                    .block();
        }
    }
}
