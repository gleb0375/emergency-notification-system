package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_adapter_db.repository.AuthAccountRepository;
import com.hhnatsiuk.api_auth_adapter_db.repository.EmailVerificationRepository;
import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.entity.EmailVerificationEntity;
import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SendVerificationRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.VerificationResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.token.EmailSendingException;
import com.hhnatsiuk.api_auth_service.exception.user.UserNotFoundException;
import com.hhnatsiuk.api_auth_service.util.OtpGenerator;
import com.hhnatsiuk.auth.api.integration.AmazonSesClient;
import com.hhnatsiuk.auth.api.services.EmailVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationServiceImpl.class);
    private static final int TTL_MIN = 60;

    private final EmailVerificationRepository emailVerificationRepository;
    private final AuthAccountRepository authAccountRepository;
    private final AmazonSesClient amazonSesClient;

    public EmailVerificationServiceImpl(EmailVerificationRepository emailVerificationRepository, AuthAccountRepository authAccountRepository, AmazonSesClient amazonSesClient) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.authAccountRepository = authAccountRepository;
        this.amazonSesClient = amazonSesClient;
    }

    @Override
    public VerificationResponseDTO sendVerificationCode(SendVerificationRequestDTO sendVerificationRequestDTO) {
        AuthAccountEntity authAccount = authAccountRepository
                .findByEmail(sendVerificationRequestDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "User does not exist!"
                ));

        String token = OtpGenerator.generate(6);

        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.builder()
                .account(authAccount)
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(TTL_MIN))
                .build();

        emailVerificationEntity = emailVerificationRepository.save(emailVerificationEntity);
        logger.debug("Saved EmailVerification [uuid={}, token={}]", emailVerificationEntity.getUuid(), token);

        try {
            amazonSesClient.sendVerification(
                    sendVerificationRequestDTO.getEmail(),
                    token,
                    TTL_MIN
            );
            logger.info("Verification email sent to {}", sendVerificationRequestDTO.getEmail());
        } catch (Exception e) {
            logger.error(
                    "Failed to send verification email to {}: {}",
                    sendVerificationRequestDTO.getEmail(),
                    e.getMessage(),
                    e
            );
            throw new EmailSendingException(HttpStatus.BAD_GATEWAY.value() ,"Unable to send verification email");
        }

        return new VerificationResponseDTO()
                .uuid(UUID.fromString(emailVerificationEntity.getUuid()))
                .email(sendVerificationRequestDTO.getEmail())
                .expiresInMinutes(TTL_MIN)
                .message("Verification code sent");

    }

    @Override
    public ConfirmEmailResponseDTO confirmEmail(ConfirmEmailRequestDTO request) {
        return null;
    }
}
