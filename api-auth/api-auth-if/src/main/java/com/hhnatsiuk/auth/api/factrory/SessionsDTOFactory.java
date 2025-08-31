package com.hhnatsiuk.auth.api.factrory;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.entity.TokenEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;

import java.time.LocalDateTime;

public interface SessionsDTOFactory {

//    TokenEntity createSession(
//            AuthAccountEntity account,
//            String refreshTokenHash,
//            String userAgent,
//            String ipAddress,
//            LocalDateTime createdAt,
//            LocalDateTime expiresAt
//    );

    SessionCreateResponseDTO sessionCreateToSessionCreateResponseDTO(
            String sessionUuid,
            String accessToken,
            String refreshToken,
            String userUuid
    );

    SessionRefreshResponseDTO createSessionToRefreshResponse(
            String sessionUuid,
            String accessToken,
            String refreshToken,
            String userUuid
    );


    TokenEntity createTokenEntityWithUser(
            String refreshTokenHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            AuthAccountEntity user,
            String userAgent,
            String ipAddress);



}
