package com.hhnatsiuk.api_auth_service.impl.factories;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.entity.TokenEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;
import com.hhnatsiuk.auth.api.factrories.SessionsDTOFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessionsDTOFactoryImpl implements SessionsDTOFactory {

    private static final Logger logger = LoggerFactory.getLogger(SessionsDTOFactoryImpl.class);

    @Override
    public TokenEntity createSession(AuthAccountEntity account, String refreshTokenHash, String userAgent, String ipAddress, LocalDateTime createdAt, LocalDateTime expiresAt) {
        logger.info("Starting session creation: userId={}, userAgent='{}', ipAddress='{}', expiresAt={}",
                account.getId(), userAgent, ipAddress, expiresAt);

        try {
            TokenEntity session = TokenEntity.builder()
                    .account(account)
                    .refreshToken(refreshTokenHash)
                    .userAgent(userAgent)
                    .ipAddress(ipAddress)
                    .createdAt(createdAt)
                    .expiresAt(expiresAt)
                    .build();

            logger.debug("SessionEntity built successfully for userId={} (sessionUuid={})",
                    account.getId(), session.getUuid());

            return session;
        } catch (Exception e) {
            logger.error("Failed to build SessionEntity for userId={}: {}", account.getId(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SessionCreateResponseDTO sessionCreateToSessionCreateResponseDTO(String sessionUuid, String accessToken, String refreshToken, String userUuid) {
        logger.info("Creating signInResponseDTO for sessionUuid: {}, userUuid: {}", sessionUuid, userUuid);
        try {
            SessionCreateResponseDTO signInResponseDTO = new SessionCreateResponseDTO();
            signInResponseDTO.setSessionUuid(sessionUuid);
            signInResponseDTO.setAccessToken(accessToken);
            signInResponseDTO.setRefreshToken(refreshToken);
            signInResponseDTO.setUserUuid(userUuid);

            logger.info("signInResponseDTO created successfully for sessionUuid: {}", sessionUuid);
            return signInResponseDTO;
        } catch (Exception e) {
            logger.error("Error creating signInResponseDTO for sessionUuid: {}, userUuid: {}", sessionUuid, userUuid, e);
            throw e;
        }
    }

    @Override
    public SessionRefreshResponseDTO createSessionToRefreshResponse(String sessionUuid, String accessToken, String refreshToken, String userUuid) {
        logger.info("Creating SessionRefreshResponseDataDTO for sessionUuid: {}, userUuid: {}", sessionUuid, userUuid);
        try {
            SessionRefreshResponseDTO sessionRefreshResponseDataDTO = new SessionRefreshResponseDTO();
            sessionRefreshResponseDataDTO.setSessionUuid(sessionUuid);
            sessionRefreshResponseDataDTO.setAccessToken(accessToken);
            sessionRefreshResponseDataDTO.setRefreshToken(refreshToken);
            sessionRefreshResponseDataDTO.setUserUuid(userUuid);

            logger.info("SessionRefreshResponseDataDTO created successfully for sessionUuid: {}", sessionUuid);
            return sessionRefreshResponseDataDTO;
        } catch (Exception e) {
            logger.error("Error creating SessionRefreshResponseDataDTO for sessionUuid: {}, userUuid: {}", sessionUuid, userUuid, e);
            throw e;
        }
    }

    @Override
    public TokenEntity createTokenEntityWithUser(String refreshTokenHash, LocalDateTime createdAt, LocalDateTime expiresAt, AuthAccountEntity user, String userAgent, String ipAddress) {
        logger.info("Creating TokenEntity with user: {}", user.getId());
        try {
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setRefreshToken(refreshTokenHash);
            tokenEntity.setCreatedAt(createdAt);
            tokenEntity.setExpiresAt(expiresAt);
            tokenEntity.setAccount(user);
            tokenEntity.setUserAgent(userAgent);
            tokenEntity.setIpAddress(ipAddress);

            logger.info("TokenEntity created successfully for user: {}", user.getId());
            return tokenEntity;
        } catch (Exception e) {
            logger.error("Error creating TokenEntity with user: {}", user.getId(), e);
            throw e;
        }
    }
}
