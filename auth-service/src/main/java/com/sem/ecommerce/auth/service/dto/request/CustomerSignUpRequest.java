package com.sem.ecommerce.auth.service.dto.request;

public record CustomerSignUpRequest(
        String email,
        String rawPassword,
        String name,
        String phoneNumber
) {
}
