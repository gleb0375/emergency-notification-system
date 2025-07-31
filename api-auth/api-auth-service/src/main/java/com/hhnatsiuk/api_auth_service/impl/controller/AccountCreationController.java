package com.hhnatsiuk.api_auth_service.impl.controller;

import com.hhnatsiuk.api_auth_if.api.generated.AccountsApi;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import com.hhnatsiuk.auth.api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountCreationController implements AccountsApi {

    private static final Logger logger = LoggerFactory.getLogger(AccountCreationController.class);

    private final UserService userService;

    public AccountCreationController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<AccountCreateResponseDTO> registerAccount(AccountCreateRequestDTO userCreateRequestDTO) {
        logger.info("Received registration request for email={}", userCreateRequestDTO.getEmail());

        AccountCreateResponseDTO accountCreated = userService.createUser(userCreateRequestDTO);

        logger.info("Account successfully created: uuid={} email={}", accountCreated.getUuid(), accountCreated.getEmail());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountCreated);
    }

}
