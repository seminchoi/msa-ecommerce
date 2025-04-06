package com.sem.ecommerce.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class PaymentMethod {
    private final UUID id;
    private final String memberId;
    private final PaymentMethodType type;
    private final String maskedNumber; // 마스킹된 카드번호 또는 계좌번호
    private final String issuer;       // 카드사 또는 은행명
    private final boolean isDefault;   // 기본 결제수단 여부
    private final LocalDateTime registeredAt;
}