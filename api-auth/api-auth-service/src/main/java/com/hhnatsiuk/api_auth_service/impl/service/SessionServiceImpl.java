package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_adapter_db.repository.TokenRepository;
import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.entity.TokenEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionRefreshResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.session.*;
import com.hhnatsiuk.api_auth_service.exception.user.UserNotFoundException;
import com.hhnatsiuk.auth.api.factrories.SessionsDTOFactory;
import com.hhnatsiuk.auth.api.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class SessionServiceImpl implements SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final SessionCreationService sessionCreationService;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final CustomSecurityService customSecurityService;
    private final CryptoUtilService cryptoUtilService;
    private final SessionsDTOFactory sessionsDTOFactory;
    private final JwtService jwtService;

    public SessionServiceImpl(AuthenticationManager authenticationManager, SessionCreationService sessionCreationService,
                              UserService userService, TokenRepository tokenRepository, CustomSecurityService customSecurityService,
                              CryptoUtilService cryptoUtilService, SessionsDTOFactory sessionsDTOFactory, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.sessionCreationService = sessionCreationService;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.customSecurityService = customSecurityService;
        this.cryptoUtilService = cryptoUtilService;
        this.sessionsDTOFactory = sessionsDTOFactory;
        this.jwtService = jwtService;
    }

    @Override
    public SessionCreateResponseDTO createSession(SessionCreateRequestDTO sessionCreateRequestDTO, String userAgent, String ipAddress) {
        logger.info("Creating session for email: {}", sessionCreateRequestDTO.getEmail());

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
        logger.info("Refreshing session with UUID: {}", sessionUuid);

        TokenEntity tokenEntity = tokenRepository.findByUuid(sessionUuid)
                .orElseThrow(() -> new SessionNotFoundException(
                            HttpStatus.NOT_FOUND.value(),
                            String.format("Session %s not found", sessionUuid)
                ));

        AuthAccountEntity user = tokenEntity.getAccount();

        if (user == null) {
            logger.error("User associated with session UUID: {} not found", sessionUuid);
            throw new UserNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "User not found during refreshing token");
        }

        validateRefreshToken(rawRefreshToken, tokenEntity);

        String newAccessToken  = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        String newHash = cryptoUtilService.hashToken(newRefreshToken);
        long ttlMs = cryptoUtilService.getRefreshTokenExpirationInMs();

        tokenEntity.setRefreshTokenHash(newHash);
        tokenEntity.setExpiresAt(LocalDateTime.now(ZoneOffset.UTC).plus(Duration.ofMillis(ttlMs)));
        tokenRepository.save(tokenEntity);

        logger.info("Session refreshed successfully for userUuid={} sessionUuid={}", user.getUuid(), tokenEntity.getUuid());

        return sessionsDTOFactory.createSessionToRefreshResponse(
                tokenEntity.getUuid(),
                newAccessToken,
                newRefreshToken,
                user.getUuid()
        );
    }

    @Override
    public void deleteSessionByUuid(String sessionUuid) {
        logger.info("Invalidating session {}", sessionUuid);
        TokenEntity tokenEntity = tokenRepository.findByUuid(sessionUuid)
                .orElseThrow(() -> new SessionNotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        String.format("Session %s not found", sessionUuid)
                ));

        if (!customSecurityService.hasRole("SUPERADMIN") && !customSecurityService.hasRole("ADMIN") && !customSecurityService.isCurrentUser(tokenEntity.getAccount().getUuid())) {
            throw new SessionAccessDeniedException(HttpStatus.FORBIDDEN.value(), "Forbidden to delete session");
        }

        try {
            tokenRepository.delete(tokenEntity);
        } catch (Exception e) {
            throw new TokenEntityDeleteException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Some problems during deleting session");
        }
        logger.info("Session {} invalidated", sessionUuid);
    }


    private void validateRefreshToken(String rawRefreshToken, TokenEntity tokenEntity) {
        LocalDateTime now = LocalDateTime.now();

        if (!tokenEntity.getExpiresAt().isAfter(now)) {
            throw new RefreshTokenExpiredException(HttpStatus.UNAUTHORIZED.value(), "Refresh Token is Expired");
        }

        String incomingHash = cryptoUtilService.hashToken(rawRefreshToken);
        if (!incomingHash.equals(tokenEntity.getRefreshTokenHash())) {
            throw new InvalidRefreshTokenException(HttpStatus.NOT_FOUND.value(), "invalid Refresh Token");
        }
    }
}
