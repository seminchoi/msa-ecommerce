package com.sem.ecommerce.auth.controller.dto;

public record CustomerSignUpRequest(
        String email,
        String rawPassword,
        String name,
        String phoneNumber
) {
}
