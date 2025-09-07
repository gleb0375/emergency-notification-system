package com.hhnatsiuk.api_auth_service.impl.service;


import com.hhnatsiuk.api_auth_adapter_db.repository.TokenRepository;
import com.hhnatsiuk.api_auth_core.domain.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.domain.entity.TokenEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.session.TokenEntitySaveException;
import com.hhnatsiuk.auth.api.factrory.SessionsDTOFactory;
import com.hhnatsiuk.auth.api.service.CryptoUtilService;
import com.hhnatsiuk.auth.api.service.JwtService;
import com.hhnatsiuk.auth.api.service.SessionCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class SessionCreationServiceImpl implements SessionCreationService {

    private static final Logger logger = LoggerFactory.getLogger(SessionCreationServiceImpl.class);

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final CryptoUtilService cryptoUtilService;
    private final SessionsDTOFactory sessionsDTOFactory;

    public SessionCreationServiceImpl(TokenRepository tokenRepository, JwtService jwtService, CryptoUtilService cryptoUtilService, SessionsDTOFactory sessionsDTOFactory) {
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.cryptoUtilService = cryptoUtilService;
        this.sessionsDTOFactory = sessionsDTOFactory;
    }

    @Override
    public SessionCreateResponseDTO createSessionForUser(AuthAccountEntity user, String userAgent, String ipAddress) {
        LocalDateTime now = LocalDateTime.now();

        Optional<TokenEntity> existingOpt = tokenRepository
                .findFirstByAccount_IdAndExpiresAtAfterOrderByExpiresAtDesc(user.getId(), now);

        String accessToken  = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        String refreshHash  = cryptoUtilService.hashToken(refreshToken);

        TokenEntity tokenEntity = existingOpt
                .map(existing -> updateExistingToken(existing, refreshHash, now, userAgent, ipAddress))
                .orElseGet(() -> createNewToken(refreshHash, now, user, userAgent, ipAddress));

        logger.info("Session {} for user {} is active until {}",
                tokenEntity.getUuid(), user.getUuid(), tokenEntity.getExpiresAt());

        return sessionsDTOFactory.sessionCreateToSessionCreateResponseDTO(
                tokenEntity.getUuid(),
                accessToken,
                refreshToken,
                user.getUuid()
        );
    }


    private TokenEntity createNewToken(String refreshTokenHash,
                                       LocalDateTime now,
                                       AuthAccountEntity user,
                                       String userAgent,
                                       String ipAddress
    ) {
        logger.debug("Creating new token for user: {}", user.getId());

        LocalDateTime expiresAt = now.plus(Duration.ofMillis(
                cryptoUtilService.getRefreshTokenExpirationInMs()
        ));

        TokenEntity tokenEntity = sessionsDTOFactory.createTokenEntityWithUser(
                refreshTokenHash,
                now,
                expiresAt,
                user,
                userAgent,
                ipAddress
        );

        try {
            return tokenRepository.save(tokenEntity);
        } catch (Exception e) {
            logger.error("Error occurred while saving new token for user: {}", user.getId(), e);
            throw new TokenEntitySaveException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Unexpected error occurred while saving the token"
            );
        }
    }

    private TokenEntity updateExistingToken(TokenEntity existingToken,
                                            String refreshTokenHash,
                                            LocalDateTime now,
                                            String userAgent,
                                            String ipAddress) {
        logger.debug("Updating existing token with UUID: {}", existingToken.getUuid());

        existingToken.setRefreshTokenHash(refreshTokenHash);
        existingToken.setCreatedAt(now);
        existingToken.setUserAgent(userAgent);
        existingToken.setIpAddress(ipAddress);
        existingToken.setExpiresAt(
                now.plus(Duration.ofMillis(
                        cryptoUtilService.getRefreshTokenExpirationInMs()
                ))
        );

        try {
            return tokenRepository.save(existingToken);
        } catch (Exception e) {
            logger.error("Error occurred while updating token with UUID: {}", existingToken.getUuid(), e);
            throw new TokenEntitySaveException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Unexpected error occurred while updating the token"
            );
        }
    }
}
