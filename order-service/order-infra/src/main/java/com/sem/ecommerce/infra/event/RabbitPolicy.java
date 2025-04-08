package com.sem.ecommerce.infra.event;

import com.sem.ecommerce.core.event.outbox.OutBoxCache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitPolicy {
    private final RabbitTemplate rabbitTemplate;
    private final OutBoxCache outBoxCache;

    @PostConstruct
    public void init() {
        // 메시지 확인 콜백 설정 - 브로커에게 메시지가 전달되었는지 확인
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                if(correlationData != null) {
                    outBoxCache.addAckedOutboxEvent(correlationData.getId())
                            .subscribe();
                }
            }
        });

        // 반환된 메시지 콜백 설정 - 라우팅할 수 없는 메시지 처리
        rabbitTemplate.setReturnsCallback(returned -> {
        });
    }

}
