package com.hhnatsiuk.api_auth_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtProperties {

    @Value("${jwt.access-token.secret}")
    private String accessTokenSecretKey;

    @Value("${jwt.refresh-token.secret}")
    private String refreshTokenSecretKey;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpirationInMs;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpirationInMs;
}
