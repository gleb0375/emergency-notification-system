package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_adapter_db.repository.AuthAccountRepository;
import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.UserResponseDTO;
import com.hhnatsiuk.api_auth_service.exception.user.UserAlreadyExistsException;
import com.hhnatsiuk.api_auth_service.validation.UserValidator;
import com.hhnatsiuk.auth.api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final AuthAccountRepository authAccountRepository;


    public UserServiceImpl(AuthAccountRepository authAccountRepository) {
        this.authAccountRepository = authAccountRepository;
    }

    @Override
    public AccountCreateResponseDTO createUser(AccountCreateRequestDTO userCreateRequestDTO) {
        logger.info("Starting user creation for email: {}", userCreateRequestDTO.getEmail());
        logger.debug("User creation request details: {}", userCreateRequestDTO);

        return null;
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


    private void validateUserCreateRequest(AccountCreateResponseDTO userCreateRequestDTO) {
        logger.info("Validating user creation request for email: {}", userCreateRequestDTO.getEmail());
        logger.debug("User creation validation details: {}", userCreateRequestDTO);
        //UserValidator.validateUserForm(userCreateRequestDTO);

        validateUserAlreadyExist(userCreateRequestDTO);

        //UserValidator.validatePassword(userCreateRequestDTO);
    }

    private void validateUserAlreadyExist(AccountCreateResponseDTO userCreateRequestDTO) {
        logger.info("Checking if user already exists for email: {}", userCreateRequestDTO.getEmail());
        List<AuthAccountEntity> usersByEmail = authAccountRepository.findAuthAccountEntityByEmail(userCreateRequestDTO.getEmail());
        logger.debug("Users found by email: {}", usersByEmail);

        if (usersByEmail != null && !usersByEmail.isEmpty()) {
            logger.warn("User already exists with email: {}", userCreateRequestDTO.getEmail());
            throw new UserAlreadyExistsException(HttpStatus.CONFLICT.value(), "User already exists!");
        }
    }
}
