package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.auth.api.services.SessionCreationService;
import com.hhnatsiuk.auth.api.services.SessionService;
import com.hhnatsiuk.auth.api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final SessionCreationService sessionCreationService;
    private final UserService userService;

    public SessionServiceImpl(AuthenticationManager authenticationManager, SessionCreationService sessionCreationService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.sessionCreationService = sessionCreationService;
        this.userService = userService;
    }

    @Override
    public SessionCreateResponseDTO createSession(SessionCreateRequestDTO sessionCreateRequestDTO, String userAgent, String ipAddress) {
        logger.info("Creating session for login: {}", sessionCreateRequestDTO.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        sessionCreateRequestDTO.getEmail(),
                        sessionCreateRequestDTO.getPassword()
                )
        );

        AuthAccountEntity user = userService.findUserByEmail(sessionCreateRequestDTO.getEmail());


        return sessionCreationService.createSessionForUser(user, userAgent, ipAddress);
    }

    @Override
    public SessionRefreshResponseDTO refreshSession(String sessionUuid, String rawRefreshToken) {
        return null;
    }

    @Override
    public void deleteSessionByUuid(String sessionUuid) {

    }
}
