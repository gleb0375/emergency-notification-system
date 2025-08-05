package com.hhnatsiuk.auth.api.factrories;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.entity.TokenEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SignInResponseDTO;

import java.time.LocalDateTime;

public interface SessionsDTOFactory {

    TokenEntity createSession(
            AuthAccountEntity account,
            String refreshTokenHash,
            String userAgent,
            String ipAddress,
            LocalDateTime createdAt,
            LocalDateTime expiresAt
    );

    SignInResponseDTO toSignInResponse(
            TokenEntity session,
            String accessToken,
            String refreshToken
    );

    SessionRefreshResponseDTO toRefreshResponse(
            TokenEntity session,
            String accessToken,
            String refreshToken
    );

}
