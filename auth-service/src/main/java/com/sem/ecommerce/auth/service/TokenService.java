package com.sem.ecommerce.auth.service;

import com.sem.ecommerce.auth.infra.jwt.JwtProvider;
import com.sem.ecommerce.auth.infra.jwt.dto.CreateAccessTokenRequest;
import com.sem.ecommerce.auth.service.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;

    public TokenResponse createToken(CreateAccessTokenRequest createAccessTokenRequest) {
        return null;
    }
}
