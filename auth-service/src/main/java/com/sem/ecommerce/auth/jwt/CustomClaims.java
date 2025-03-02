package com.sem.ecommerce.auth.jwt;

public enum CustomClaims {
    USERNAME("username"),
    NAME("name"),
    EMAIL("email"),
    ROLE("role"),
    GENDER("gender")
    ;

    CustomClaims(String claimName) {
        this.claimName = claimName;
    }

    private final String claimName;
}
