package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_adapter_db.repository.TokenRepository;
import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.entity.TokenEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.session.SessionAccessDeniedException;
import com.hhnatsiuk.api_auth_service.exception.session.SessionNotFoundException;
import com.hhnatsiuk.api_auth_service.exception.session.TokenEntityDeleteException;
import com.hhnatsiuk.auth.api.services.CustomSecurityService;
import com.hhnatsiuk.auth.api.services.SessionCreationService;
import com.hhnatsiuk.auth.api.services.SessionService;
import com.hhnatsiuk.auth.api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final SessionCreationService sessionCreationService;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final CustomSecurityService customSecurityService;

    public SessionServiceImpl(AuthenticationManager authenticationManager, SessionCreationService sessionCreationService,
                              UserService userService, TokenRepository tokenRepository, CustomSecurityService customSecurityService) {
        this.authenticationManager = authenticationManager;
        this.sessionCreationService = sessionCreationService;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.customSecurityService = customSecurityService;
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
        logger.info("Invalidating session {}", sessionUuid);
        TokenEntity tokenEntity = tokenRepository.findByUuid(sessionUuid)
                .orElseThrow(() -> new SessionNotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        String.format("Session %s not found", sessionUuid)
                ));

        if (!customSecurityService.hasRole("ADMIN") && !customSecurityService.isCurrentUser(tokenEntity.getAccount().getUuid())) {
            throw new SessionAccessDeniedException(HttpStatus.FORBIDDEN.value(), "Forbidden to delete session");
        }

        try {
            tokenRepository.delete(tokenEntity);
        } catch (Exception e) {
            throw new TokenEntityDeleteException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Some problems during deleting session");
        }
        logger.info("Session {} invalidated", sessionUuid);
    }
}
