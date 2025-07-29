package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_adapter_db.repository.AuthAccountRepository;
import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.UserResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.user.UserAlreadyExistsException;
import com.hhnatsiuk.auth.api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int VERIFICATION_TTL_MIN = 60;
    private final AuthAccountRepository authAccountRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(AuthAccountRepository authAccountRepository, PasswordEncoder passwordEncoder) {
        this.authAccountRepository = authAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AccountCreateResponseDTO createUser(AccountCreateRequestDTO dto) {
        logger.info("Creating account for email={}", dto.getEmail());

        String email = dto.getEmail().trim().toLowerCase();
        if (authAccountRepository.existsByEmail(email)) {
            logger.warn("Email already in use: {}", email);
            throw new UserAlreadyExistsException(
                    HttpStatus.CONFLICT.value(),
                    "An account with this email already exists"
            );
        }

        AuthAccountEntity acct = new AuthAccountEntity();
        acct.setEmail(email);
        acct.setPassword(passwordEncoder.encode(dto.getPassword()));
        acct = authAccountRepository.save(acct);

        // TODO: сгенерировать и сохранить токен верификации
        // UUID token = UUID.randomUUID();
        // verificationRepo.save(new EmailVerificationEntity(token.toString(), ..., acct));

        return new AccountCreateResponseDTO()
                .uuid(UUID.fromString(acct.getUuid()))
                .email(acct.getEmail())
                .expiresInMinutes(VERIFICATION_TTL_MIN)
                .message("Account created; verification code sent to email");
    }

    @Override
    public UserResponseDTO findUserByUuid(String uuid) {
        return null;
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(String uuid) {

    }

    @Override
    public AuthAccountEntity findUserByEmail(String email) {
        return null;
    }
}
