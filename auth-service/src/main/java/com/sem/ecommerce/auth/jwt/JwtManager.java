package com.sem.ecommerce.auth.jwt;

import com.sem.ecommerce.auth.command.CreateTokenCommand;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class JwtManager {
    private final String ISSUER = "https://sem.ecommerce.com";
    private final long EXPIRATION_MILLISEC = 1000 * 60 * 60 * 24;
    private final AtomicReference<SecretKey> KEY = new AtomicReference<>();

    public String generateToken(CreateTokenCommand createTokenCommand) {
        //TODO: Key 생성 및 획득 로직 분리
        //TODO: DB 적용하여 주기적 키 교체 로직 구현
        //TODO: Vault, Cloud 를 활용하여 키 이중 암호화 적용
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
                .subject(createTokenCommand.username())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLISEC))
                .claim("name", createTokenCommand.name())
                .claim("email", createTokenCommand.name())
                .claim("gender", createTokenCommand.gender().toString())
                .claim("role", createTokenCommand.role().toString())

                .signWith(KEY.get())
                .compact();
    }
}
