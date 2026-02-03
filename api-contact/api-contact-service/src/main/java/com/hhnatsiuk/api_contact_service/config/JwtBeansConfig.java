package com.hhnatsiuk.api_contact_service.config;

import com.hhnatsiuk.api_contact_service.impl.service.ProfileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class JwtBeansConfig {

    private static final Logger logger = LoggerFactory.getLogger(JwtBeansConfig.class);

    @Bean
    public JwtDecoder jwtDecoder(TokenSecurityProperties props) {
        SecretKey key = hmacSha256Key(props.getAccessTokenSecretKey());
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).build();

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(props.getIssuer());
        OAuth2TokenValidator<Jwt> withTimestamps =
                new JwtTimestampValidator(Duration.ofSeconds(props.getClockSkewSeconds()));
        OAuth2TokenValidator<Jwt> withAudience = token -> {
            List<String> aud = token.getAudience();
            return (aud != null && aud.contains(props.getAudience()))
                    ? OAuth2TokenValidatorResult.success()
                    : OAuth2TokenValidatorResult.failure(
                    new OAuth2Error("invalid_token", "Invalid audience", null)
            );
        };

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withTimestamps, withAudience));
        return decoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter conv = new JwtAuthenticationConverter();
        conv.setJwtGrantedAuthoritiesConverter(jwt -> {

            logger.debug(
                    "JWT authenticated: iss={}, aud={}, sub={}, uid={}, roles={}, exp={}",
                    jwt.getIssuer(),
                    jwt.getAudience(),
                    jwt.getSubject(),
                    jwt.getClaimAsString("uid"),
                    jwt.getClaimAsStringList("roles"),
                    jwt.getExpiresAt()
            );

            List<String> roles = jwt.getClaimAsStringList("roles");

            if (roles == null) return List.of();

            return roles.stream()
                    .filter(r -> r != null && !r.isBlank())
                    .map(r -> "ROLE_" + r.trim().toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .collect(Collectors.toUnmodifiableList());
        });
        return conv;
    }

    private SecretKey hmacSha256Key(String base64) {
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}
