package com.sem.ecommerce.auth.service.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
