package com.hhnatsiuk.api_auth_service.impl.controller;

import com.hhnatsiuk.api_auth_if.api.generated.VerificationsApi;
import com.hhnatsiuk.api_auth_if.model.generated.SendVerificationRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.VerificationResponseDTO;
import com.hhnatsiuk.auth.api.services.EmailVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import software.amazon.awssdk.http.HttpStatusCode;

@Controller
public class VerificationEmailController implements VerificationsApi {

    private static final Logger logger = LoggerFactory.getLogger(VerificationEmailController.class);

    private final EmailVerificationService emailVerificationService;

    public VerificationEmailController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }


    @Override
    public ResponseEntity<VerificationResponseDTO> sendVerificationCode (SendVerificationRequestDTO sendVerificationRequestDTO) {
        logger.info("");

        VerificationResponseDTO verificationResponseDTO = emailVerificationService.sendVerificationCode(sendVerificationRequestDTO);

        logger.info("Verification code has been sent");

        return ResponseEntity.
                status(HttpStatus.OK)
                .body(verificationResponseDTO);
    }

}
