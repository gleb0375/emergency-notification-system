package com.hhnatsiuk.api_auth_service.impl.controller;

import com.hhnatsiuk.api_auth_if.api.generated.SessionsApi;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;
import com.hhnatsiuk.api_auth_service.impl.service.SessionServiceImpl;
import com.hhnatsiuk.auth.api.services.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

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
    public ResponseEntity<SessionCreateResponseDTO> signIn(SessionCreateRequestDTO sessionCreateRequestDTO) {
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

}
