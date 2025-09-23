package com.hhnatsiuk.api_contact_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TokenSecurityProperties {

    @Value("${jwt.access-token.secret}")
    private String accessTokenSecretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.clock-skew-seconds:15}")
    private long clockSkewSeconds;
}
