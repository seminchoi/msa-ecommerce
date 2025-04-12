package com.sem.ecommerce.domain.order.port;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderServicePort {
    /**
     * 만료된 주문 생성 이벤트 처리
     * @param orderId 처리할 주문 ID
     * @return 처리 완료 시그널
     */
    Mono<Void> handleExpiredOrderCreated(UUID orderId);

    // 다른 만료 이벤트 처리 메서드 추가 가능
}