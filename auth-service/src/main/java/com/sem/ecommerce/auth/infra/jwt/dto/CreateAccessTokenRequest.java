package com.sem.ecommerce.auth.infra.jwt.dto;

import com.sem.ecommerce.auth.domain.Role;
import lombok.Builder;

@Builder
public record CreateAccessTokenRequest(
        String email,
        String name,
        String phoneNumber,
        Role role
) {
}
