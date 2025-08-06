package com.hhnatsiuk.api_auth_service.impl.service;


import com.hhnatsiuk.api_auth_adapter_db.repository.TokenRepository;
import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.entity.TokenEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.token.TokenEntitySaveException;
import com.hhnatsiuk.auth.api.factrories.SessionsDTOFactory;
import com.hhnatsiuk.auth.api.services.CryptoUtilService;
import com.hhnatsiuk.auth.api.services.JwtService;
import com.hhnatsiuk.auth.api.services.SessionCreationService;
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
    public SessionCreateResponseDTO createSessionForUser(AuthAccountEntity user) {
        // 1. Берём текущий момент как LocalDateTime
        LocalDateTime now = LocalDateTime.now();

        // 2. Ищем действующую сессию (expiresAt — тоже LocalDateTime)
        Optional<TokenEntity> existingOpt = tokenRepository
                .findFirstByAccount_IdAndExpiresAtAfterOrderByExpiresAtDesc(user.getId(), now);

        // 3. Генерим токены
        String accessToken  = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        String refreshHash  = cryptoUtilService.hashToken(refreshToken);

        // 4. Обновляем или создаём новую запись
        TokenEntity tokenEntity = existingOpt
                .map(existing -> updateExistingToken(existing, refreshHash, now))
                .orElseGet(() -> createNewToken(refreshHash, now, user));

        logger.info("Session {} for user {} is active until {}",
                tokenEntity.getUuid(), user.getUuid(), tokenEntity.getExpiresAt());

        // 5. Формируем ответ
        return sessionsDTOFactory.sessionCreateToSessionCreateResponseDTO(
                tokenEntity.getUuid(),
                accessToken,
                refreshToken,
                user.getUuid()
        );
    }


    private TokenEntity createNewToken(String refreshTokenHash,
                                       LocalDateTime now,
                                       AuthAccountEntity user) {
        logger.debug("Creating new token for user: {}", user.getId());

        // рассчитываем expiresAt как now + TTL
        LocalDateTime expiresAt = now.plus(Duration.ofMillis(
                cryptoUtilService.getRefreshTokenExpirationInMs()
        ));

        TokenEntity tokenEntity = sessionsDTOFactory.createTokenEntityWithUser(
                refreshTokenHash,
                now,
                expiresAt,
                user
        );

        try {
            return tokenRepository.save(tokenEntity);
        } catch (Exception e) {
            logger.error("Error occurred while saving new token for user: {}", user.getId(), e);
            throw new TokenEntitySaveException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "%#sessionsService.anUnexpectedErrorOccurredWhileSavingTheToken#%"
            );
        }
    }

    private TokenEntity updateExistingToken(TokenEntity existingToken,
                                            String refreshTokenHash,
                                            LocalDateTime now) {
        logger.debug("Updating existing token with UUID: {}", existingToken.getUuid());

        existingToken.setRefreshToken(refreshTokenHash);
        existingToken.setCreatedAt(now);
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
                    "%#sessionsService.anUnexpectedErrorOccurredWhileUpdatingTheToken#%"
            );
        }
    }
}
