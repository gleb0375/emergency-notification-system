package com.hhnatsiuk.auth.api.services;

import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SignInRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SignInResponseDTO;

public interface SessionService {

    SignInResponseDTO createSession(SignInRequestDTO signInRequestDTO);

    SessionRefreshResponseDTO refreshSession(String sessionUuid, String rawRefreshToken);

    void deleteSessionByUuid(String sessionUuid);

}
