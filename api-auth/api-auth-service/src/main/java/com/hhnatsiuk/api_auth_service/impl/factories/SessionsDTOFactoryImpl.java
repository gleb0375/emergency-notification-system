package com.hhnatsiuk.api_auth_service.impl.factories;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.entity.SessionEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SignInResponseDTO;
import com.hhnatsiuk.auth.api.factrories.SessionsDTOFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class SessionsDTOFactoryImpl implements SessionsDTOFactory {

    private static final Logger logger = LoggerFactory.getLogger(SessionsDTOFactoryImpl.class);

    @Override
    public SessionEntity createSession(AuthAccountEntity account, String refreshTokenHash, String userAgent, String ipAddress, LocalDateTime createdAt, LocalDateTime expiresAt) {
        logger.info("Starting session creation: userId={}, userAgent='{}', ipAddress='{}', expiresAt={}",
                account.getId(), userAgent, ipAddress, expiresAt);

        try {
            SessionEntity session = SessionEntity.builder()
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
    public SignInResponseDTO toSignInResponse(SessionEntity session, String accessToken, String refreshToken) {
        return null;
    }

    @Override
    public SessionRefreshResponseDTO toRefreshResponse(SessionEntity session, String accessToken, String refreshToken) {
        return null;
    }
}
