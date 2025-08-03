package com.hhnatsiuk.api_auth_service.impl.controller;

import com.hhnatsiuk.api_auth_if.api.generated.VerificationsApi;
import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SendVerificationRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.VerificationResponseDTO;
import com.hhnatsiuk.auth.api.services.EmailVerificationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Transactional
public class VerificationEmailController implements VerificationsApi {

    private static final Logger logger = LoggerFactory.getLogger(VerificationEmailController.class);

    private final EmailVerificationService emailVerificationService;

    public VerificationEmailController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }


    @Override
    public ResponseEntity<VerificationResponseDTO> sendVerificationCode (SendVerificationRequestDTO sendVerificationRequestDTO) {
        logger.info("Resend verification code request for email={}", sendVerificationRequestDTO.getEmail());

        VerificationResponseDTO verificationResponseDTO = emailVerificationService.sendVerificationCode(sendVerificationRequestDTO);

        logger.info(
                "Verification code resent to {} (expires in {} minutes)",
                verificationResponseDTO.getEmail(), verificationResponseDTO.getExpiresInMinutes()
        );

        return ResponseEntity.
                status(HttpStatus.OK)
                .body(verificationResponseDTO);
    }

    @Override
    public ResponseEntity<ConfirmEmailResponseDTO> confirmEmail (String token, ConfirmEmailRequestDTO confirmEmailRequestDTO) {
        logger.info("Received confirmEmail request for email={} with token={}", confirmEmailRequestDTO.getEmail(), token);

        ConfirmEmailResponseDTO confirmEmailResponseDTO = emailVerificationService.confirmEmail(token, confirmEmailRequestDTO);

        return ResponseEntity.
                status(HttpStatus.OK)
                .body(confirmEmailResponseDTO);
    }

}
