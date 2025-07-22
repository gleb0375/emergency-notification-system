package com.hhnatsiuk.auth.api.factrories;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.entity.SessionEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SignInResponseDTO;

import java.time.LocalDateTime;

public interface SessionsDTOFactory {

    SessionEntity createSession(
            AuthAccountEntity account,
            String refreshTokenHash,
            String userAgent,
            String ipAddress,
            LocalDateTime createdAt,
            LocalDateTime expiresAt
    );

    SignInResponseDTO toSignInResponse(
            SessionEntity session,
            String accessToken,
            String refreshToken
    );

    SessionRefreshResponseDTO toRefreshResponse(
            SessionEntity session,
            String accessToken,
            String refreshToken
    );

}
