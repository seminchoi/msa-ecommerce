package com.sem.ecommerce.auth.infra.jwt;

import lombok.Getter;

@Getter
public enum CustomClaims {
    EMAIL("email"),
    NAME("name"),
    ROLE("role"),
    PHONE_NUMBER("phoneNumber"),
    ;

    CustomClaims(String claimName) {
        this.claimName = claimName;
    }

    private final String claimName;
}
