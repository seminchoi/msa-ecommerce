package com.sem.ecommerce.auth.infra.jwt;

import com.sem.ecommerce.auth.domain.Member;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class JwtProvider {
    private final String ISSUER = "https://sem.ecommerce.com";
    private final long EXPIRATION_MILLISEC = 1000 * 60 * 60 * 24;
    private final AtomicReference<SecretKey> KEY = new AtomicReference<>();

    public String generateToken(Member member) {
        //TODO: Key 생성 및 획득 로직 분리
        //TODO: Vault 를 활용하여 키 이중 암호화 및 주기적 교체 적용
        if (KEY.get() == null) {
            synchronized (KEY) {
                if (KEY.get() == null) {
                    SecretKey key = Jwts.SIG.HS512.key().build();
                    KEY.set(key);
                }
            }
        }

        return Jwts.builder()
                .issuer(ISSUER)

                .claim(CustomClaims.NAME.getClaimName(), member.getName())
                .claim(CustomClaims.EMAIL.getClaimName(), member.getEmail())
                .claim(CustomClaims.PHONE_NUMBER.getClaimName(), member.getPhoneNumber())

                .claim(CustomClaims.ROLE.getClaimName(), member.getRole().toString())

                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLISEC))

                .signWith(KEY.get())
                .compact();
    }
}
