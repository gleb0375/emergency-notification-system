package com.hhnatsiuk.api_auth_service.impl.controller;

import com.hhnatsiuk.api_auth_if.api.generated.SessionsApi;
import com.hhnatsiuk.api_auth_if.model.generated.*;
import com.hhnatsiuk.auth.api.services.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController implements SessionsApi {

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    private final SessionService sessionService;
    private final HttpServletRequest request;

    public SessionController(SessionService sessionService,
                             HttpServletRequest request) {
        this.sessionService = sessionService;
        this.request = request;
    }


    @Override
    public ResponseEntity<SessionCreateResponseDTO> createSession(SessionCreateRequestDTO sessionCreateRequestDTO) {
        String ua = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        logger.info("Sign-in request from ip={} ua={} for email={}", ip, ua, sessionCreateRequestDTO.getEmail());

        logger.info("Received sign-in request for email={}", sessionCreateRequestDTO.getEmail());

        SessionCreateResponseDTO createdSession = sessionService.createSession(sessionCreateRequestDTO, ua, ip);

        logger.info("Created session: sessionUuid={}, userUuid={}",
                createdSession.getSessionUuid(),
                createdSession.getUserUuid());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createdSession);
    }


    @Override
    public ResponseEntity<SessionRefreshResponseDTO> refreshSession(String sessionUuid, SessionRefreshRequestDTO sessionRefreshRequestDTO) {
        logger.info("Received request to refresh session with UUID: {}", sessionUuid);
        logger.debug("Session refresh request details: {}", sessionRefreshRequestDTO);

        SessionRefreshResponseDTO sessionRefreshResponseDTO = sessionService.refreshSession(sessionUuid, sessionRefreshRequestDTO.getRefreshToken());

        logger.info("Session refreshed successfully for UUID: {}", sessionUuid);

        return ResponseEntity.ok(sessionRefreshResponseDTO);
    }

    @Override
    public ResponseEntity<SessionDeleteResponseDTO> deleteSession(String sessionUuid) {
        logger.info("Received logout request for sessionUuid={}", sessionUuid);

        sessionService.deleteSessionByUuid(sessionUuid);

        return ResponseEntity
                .noContent()
                .build();
    }

}
