package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_adapter_db.mapper.AccountCreateResponseMapper;
import com.hhnatsiuk.api_auth_adapter_db.mapper.UserResponseMapper;
import com.hhnatsiuk.api_auth_adapter_db.repository.AuthAccountRepository;
import com.hhnatsiuk.api_auth_adapter_db.repository.RoleRepository;
import com.hhnatsiuk.api_auth_core.domain.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.domain.entity.RoleEntity;
import com.hhnatsiuk.api_auth_core.outbox.OutboxAppender;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.UserResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.user.RoleNotFoundException;
import com.hhnatsiuk.api_auth_service.exception.user.UserAlreadyExistsException;
import com.hhnatsiuk.api_auth_service.exception.user.UserNotFoundException;
import com.hhnatsiuk.api_auth_service.validation.UserValidator;
import com.hhnatsiuk.auth.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${auth.verification.ttl-seconds:3600}")
    private long verificationTtlSeconds;

    private final AuthAccountRepository authAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserResponseMapper userResponseMapper;
    private final AccountCreateResponseMapper accountCreateResponseMapper;
    private final OutboxAppender outboxAppender;

    public UserServiceImpl(AuthAccountRepository authAccountRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           UserResponseMapper userResponseMapper,
                           AccountCreateResponseMapper accountCreateResponseMapper,
                           OutboxAppender outboxAppender) {
        this.authAccountRepository = authAccountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userResponseMapper = userResponseMapper;
        this.accountCreateResponseMapper = accountCreateResponseMapper;
        this.outboxAppender = outboxAppender;
    }

    @Override
    @Transactional
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

        RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException(HttpStatus.NOT_FOUND.value(), "Seed role USER not found"));
        authAccountEntity.getRoles().add(userRole);

        authAccountEntity = authAccountRepository.save(authAccountEntity);

        logger.debug("Created AuthAccountEntity [id={}, uuid={}, email={}]",
                authAccountEntity.getId(), authAccountEntity.getUuid(), authAccountEntity.getEmail());


        int ttlMinutes = (int) Math.ceil(verificationTtlSeconds / 60.0);

        return accountCreateResponseMapper.toDto(
                authAccountEntity,
                ttlMinutes,
                "Account created; verification code sent to email"
        );
    }

    @Override
    public UserResponseDTO findUserByUuid(String uuid) {
        logger.info("Finding user by uuid={}", uuid);

        AuthAccountEntity account = authAccountRepository.findAuthAccountEntityByUuid(uuid)
                .orElseThrow(() -> {
                    logger.warn("User with uuid='{}' not found", uuid);
                    return new UserNotFoundException(
                            HttpStatus.NOT_FOUND.value(),
                            String.format("User with uuid '%s' not found", uuid)
                    );
                });

        UserResponseDTO userResponseDTO = userResponseMapper.toDto(account);
        logger.debug("Found user uuid={}, email={}, roles={}", uuid, userResponseDTO.getEmail(), userResponseDTO.getRoles());
        return userResponseDTO;
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        logger.info("Fetching all users");

        List<AuthAccountEntity> accounts = authAccountRepository.findAll();
        List<UserResponseDTO> result = userResponseMapper.toDtoList(accounts);

        logger.debug("Fetched {} users", result.size());
        return result;
    }

    @Override
    public void deleteUser(String uuid) {
        logger.info("Deleting user uuid={}", uuid);

        AuthAccountEntity account = authAccountRepository.findAuthAccountEntityByUuid(uuid)
                .orElseThrow(() -> {
                    logger.warn("User with uuid='{}' not found for deletion", uuid);
                    return new UserNotFoundException(
                            HttpStatus.NOT_FOUND.value(),
                            String.format("User with uuid '%s' not found", uuid)
                    );
                });

        authAccountRepository.delete(account);

        logger.debug("Appending outbox event: user.deleted for accountUuid={}", uuid);
        outboxAppender.appendUserDeleted(uuid);
        logger.info("Outbox event persisted: user.deleted for accountUuid={}", uuid);

        logger.info("User uuid={} deleted", uuid);
    }

    @Override
    public AuthAccountEntity findUserByEmail(String email) {
        logger.info("Finding user by email={}", email);
        String normalizedEmail = email.trim().toLowerCase();

        return authAccountRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    logger.warn("User with email='{}' not found", normalizedEmail);
                    return new UserNotFoundException(
                            HttpStatus.NOT_FOUND.value(),
                            String.format("User with email '%s' not found", normalizedEmail)
                    );
                });
    }
}
