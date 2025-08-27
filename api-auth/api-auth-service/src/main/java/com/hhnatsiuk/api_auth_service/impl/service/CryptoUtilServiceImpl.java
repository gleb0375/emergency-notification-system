package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_service.config.TokenSecurityProperties;
import com.hhnatsiuk.api_auth_service.exception.session.TokenHashingException;
import com.hhnatsiuk.auth.api.services.CryptoUtilService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class CryptoUtilServiceImpl implements CryptoUtilService {

    private static final Logger logger = LogManager.getLogger(CryptoUtilServiceImpl.class);
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final TokenSecurityProperties tokenSecurityProperties;

    public CryptoUtilServiceImpl(TokenSecurityProperties tokenSecurityProperties) {
        this.tokenSecurityProperties = tokenSecurityProperties;
    }

    @Override
    public String hashToken(String token) {
        logger.info("Hashing token: {}", token);
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);

            mac.init(getTokenHashKey());

            byte[] hashedBytes = mac.doFinal(token.getBytes(StandardCharsets.UTF_8));
            String hashedToken = Base64.getUrlEncoder().withoutPadding().encodeToString(hashedBytes);
            logger.debug("Token hashed successfully: {}", hashedToken);

            return hashedToken;
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            logger.error("Failed to hash token", ex);
            throw new TokenHashingException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "errorWhileHashingTheToken");
        }
    }

    @Override
    public SecretKey getAccessTokenSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(tokenSecurityProperties.getAccessTokenSecretKey());
        return new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
    }

    @Override
    public SecretKey getRefreshTokenKey() {
        byte[] keyBytes = Base64.getDecoder().decode(tokenSecurityProperties.getRefreshTokenSecretKey());
        return new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
    }

    private SecretKey getTokenHashKey() {
        byte[] keyBytes = Base64.getDecoder().decode(tokenSecurityProperties.getTokenHashSecret());
        return new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
    }

    @Override
    public long getAccessTokenExpirationInMs() {
        return tokenSecurityProperties.getAccessTokenExpirationInMs();
    }

    @Override
    public long getRefreshTokenExpirationInMs() {
        return tokenSecurityProperties.getRefreshTokenExpirationInMs();
    }
}
