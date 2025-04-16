package com.sem.ecommerce.core.event.outbox;

public interface EventType {
    String name();
    /**
     * 이벤트 타입 문자열 반환 (예: "order.created")
     */
    String getEventKey();

    /**
     * 출력 바인딩 채널 반환 (예: "orderProcessor-out-0")
     */
    String getOutputBinding();

    /**
     * 이벤트 만료 시간 반환 (분 단위)
     * 만료 시간이 0 이하인 경우 만료되지 않음
     */
    int getExpirationTimeInMinutes();
}