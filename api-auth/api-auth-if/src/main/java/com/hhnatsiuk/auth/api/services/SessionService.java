package com.hhnatsiuk.auth.api.services;

import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;

public interface SessionService {

    SessionCreateResponseDTO createSession(SessionCreateRequestDTO sessionCreateRequestDTO, String userAgent, String ipAddress);

    SessionRefreshResponseDTO refreshSession(String sessionUuid, String rawRefreshToken);

    void deleteSessionByUuid(String sessionUuid);

}
