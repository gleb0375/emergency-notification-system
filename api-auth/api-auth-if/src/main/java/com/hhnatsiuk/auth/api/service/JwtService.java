package com.hhnatsiuk.auth.api.service;

import com.hhnatsiuk.api_auth_core.domain.entity.AuthAccountEntity;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.function.Function;

public interface JwtService {

    String extractEmail(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateAccessToken(AuthAccountEntity userDetails);
    String generateRefreshToken(AuthAccountEntity userDetails);

    boolean validateAccessToken(String token);
    boolean validateRefreshToken(String token);
    boolean isTokenExpired(String token);

    String resolveToken(HttpServletRequest request);

    List<String> extractRoles(String token);
}
