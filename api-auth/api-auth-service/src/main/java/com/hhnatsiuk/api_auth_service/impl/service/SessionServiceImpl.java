package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SignInRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SignInResponseDTO;
import com.hhnatsiuk.auth.api.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final AuthenticationManager authenticationManager;

    public SessionServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public SignInResponseDTO createSession(SignInRequestDTO signInRequestDTO) {
        logger.info("Creating session for login: {}", signInRequestDTO.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequestDTO.getEmail(),
                        signInRequestDTO.getPassword()
                )
        );

        //TODO continue
        return null;
    }

    @Override
    public SessionRefreshResponseDTO refreshSession(String sessionUuid, String rawRefreshToken) {
        return null;
    }

    @Override
    public void deleteSessionByUuid(String sessionUuid) {

    }
}
