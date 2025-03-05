package com.sem.ecommerce.domain;

public enum OrderState {
    CREATED,                // 주문 생성됨
    PAYMENT_COMPLETED,      // 결제 완료

    SHIPPING_READY,         // 배송 준비 중
    SHIPPING_STARTED,       // 배송 시작됨
    SHIPPING_IN_TRANSIT,    // 배송 중
    SHIPPING_COMPLETED,     // 배송 완료

    COMPLETED,              // 주문 완료
    CANCELLED,              // 주문 취소됨
    RETURN_REQUESTED,       // 반품 요청됨
    RETURNED,               // 반품 완료
    EXCHANGE_REQUESTED,     // 교환 요청됨
    EXCHANGED               // 교환 완료
}
