package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.auth.api.services.CryptoUtilService;
import com.hhnatsiuk.auth.api.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger logger = LogManager.getLogger(JwtServiceImpl.class);
    private final CryptoUtilService cryptoUtilService;

    public JwtServiceImpl(CryptoUtilService cryptoUtilService) {
        this.cryptoUtilService = cryptoUtilService;
    }

    @Override
    public String extractEmail(String token) {
        logger.debug("Extracting email (subject) from token");
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parseToken(token, cryptoUtilService.getAccessTokenSecretKey());
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateAccessToken(AuthAccountEntity userDetails) {
        logger.info("Generating access token for user: {}", userDetails.getEmail());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + cryptoUtilService.getAccessTokenExpirationInMs());

        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .claim("roles", userDetails.getRoles())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(cryptoUtilService.getAccessTokenSecretKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(AuthAccountEntity userDetails) {
        logger.info("Generating refresh token for user: {}", userDetails.getEmail());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + cryptoUtilService.getRefreshTokenExpirationInMs());

        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(cryptoUtilService.getRefreshTokenKey())
                .compact();
    }

    @Override
    public boolean validateAccessToken(String token) {
        logger.debug("Validating access token");
        return validateToken(token, cryptoUtilService.getAccessTokenSecretKey());
    }

    @Override
    public boolean validateRefreshToken(String token) {
        logger.debug("Validating refresh token");
        return validateToken(token, cryptoUtilService.getRefreshTokenKey());
    }

    @Override
    public boolean isTokenExpired(String token) {
        logger.debug("Checking if token is expired");
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (JwtException ex) {
            logger.error("Failed to extract expiration from token", ex);
            return true;
        }
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        logger.debug("Resolving token from request");
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            logger.debug("Resolved token: {}", token);
            return token;
        }

        logger.warn("No token found in request header");
        return null;
    }

    @Override
    public List<String> extractRoles(String token) {
        logger.debug("Extracting roles from token");
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }


    private Claims parseToken(String token, SecretKey key) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException ex) {
            logger.error("Token parsing/validation failed", ex);
            throw ex;
        }
    }

    private boolean validateToken(String token, SecretKey key) {
        try {
            parseToken(token, key);
            logger.debug("Token validation successful");
            return true;
        } catch (JwtException ex) {
            logger.error("Error while validating token", ex);
            return false;
        }
    }
}
