package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_adapter_db.repository.AuthAccountRepository;
import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.UserResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.user.UserAlreadyExistsException;
import com.hhnatsiuk.api_auth_service.exception.user.UserNotFoundException;
import com.hhnatsiuk.api_auth_service.validation.UserValidator;
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
    public AccountCreateResponseDTO createUser(AccountCreateRequestDTO accountCreateRequest) {
        logger.info("Creating account for email={}", accountCreateRequest.getEmail());

        UserValidator.validateForCreate(accountCreateRequest, authAccountRepository);

        String email = accountCreateRequest.getEmail().trim().toLowerCase();
        if (authAccountRepository.existsByEmail(email)) {
            logger.warn("Email already in use: {}", email);
            throw new UserAlreadyExistsException(
                    HttpStatus.CONFLICT.value(),
                    "An account with this email already exists"
            );
        }

        AuthAccountEntity authAccountEntity = new AuthAccountEntity();
        authAccountEntity.setEmail(email);
        authAccountEntity.setPassword(passwordEncoder.encode(accountCreateRequest.getPassword()));
        authAccountEntity = authAccountRepository.save(authAccountEntity);

        AccountCreateResponseDTO response = new AccountCreateResponseDTO()
                .uuid(UUID.fromString(authAccountEntity.getUuid()))
                .email(authAccountEntity.getEmail())
                .expiresInMinutes(VERIFICATION_TTL_MIN)
                .message("Account created; verification code sent to email");

        logger.debug("Created AuthAccountEntity [id={}, uuid={}, email={}]",
                authAccountEntity.getId(), authAccountEntity.getUuid(), authAccountEntity.getEmail());

        return response;
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
        logger.info("Finding user by email: {}", email);
        String normalizedEmail = email.trim().toLowerCase();

        return authAccountRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UserNotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        String.format("User with email '%s' not found", normalizedEmail)
                ));
    }
}
