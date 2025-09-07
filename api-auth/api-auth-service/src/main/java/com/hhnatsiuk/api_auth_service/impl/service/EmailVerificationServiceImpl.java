package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_adapter_db.repository.AuthAccountRepository;
import com.hhnatsiuk.api_auth_adapter_db.repository.EmailVerificationRepository;
import com.hhnatsiuk.api_auth_core.domain.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.domain.entity.EmailVerificationEntity;
import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SendVerificationRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.VerificationResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.verification.EmailSendingException;
import com.hhnatsiuk.api_auth_service.exception.user.UserNotFoundException;
import com.hhnatsiuk.api_auth_service.exception.verification.VerificationNotFoundException;
import com.hhnatsiuk.api_auth_service.util.OtpGenerator;
import com.hhnatsiuk.auth.api.integration.AmazonSesClient;
import com.hhnatsiuk.auth.api.service.EmailVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationServiceImpl.class);

    @Value("${auth.verification.ttl-seconds:3600}")
    private long verificationTtlSeconds;

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
                .expiresAt(LocalDateTime.now().plusSeconds(verificationTtlSeconds))
                .build();

        emailVerificationEntity = emailVerificationRepository.save(emailVerificationEntity);
        logger.debug("Saved EmailVerification [uuid={}, token={}]", emailVerificationEntity.getUuid(), token);

        int ttlMinutes = (int) Math.ceil(verificationTtlSeconds / 60.0);

        try {
            amazonSesClient.sendVerification(
                    sendVerificationRequestDTO.getEmail(),
                    token,
                    ttlMinutes
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
                .expiresInMinutes(ttlMinutes)
                .message("Verification code sent");

    }

    @Override
    public ConfirmEmailResponseDTO confirmEmail(String token, ConfirmEmailRequestDTO request) {
        logger.info("Attempting to confirm email={} with token={}", request.getEmail(), token);

        AuthAccountEntity account = authAccountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "User not found during confirmation email"
                ));

        EmailVerificationEntity latestVerificationEntity = emailVerificationRepository.findTopByAccountOrderByCreatedAtDesc(account)
                .orElseThrow(() -> new VerificationNotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "Verification entity not found"
                ));

        if (!latestVerificationEntity.getToken().equals(token)) {
            logger.warn("Provided token does not match latest for {}", request.getEmail());
            throw new VerificationNotFoundException(
                    HttpStatus.NOT_FOUND.value(),
                    "Verification code not found"
            );
        }

        account.verifyEmail();
        authAccountRepository.save(account);
        logger.info("Email {} marked as verified", request.getEmail());

        emailVerificationRepository.deleteAllByAccount(account);
        logger.debug("All verification tokens for {} have been deleted", account.getUuid());

        logger.info("Email confirmation successful for {}", request.getEmail());;

        return new ConfirmEmailResponseDTO()
                .message("Email successfully verified");
    }
}
