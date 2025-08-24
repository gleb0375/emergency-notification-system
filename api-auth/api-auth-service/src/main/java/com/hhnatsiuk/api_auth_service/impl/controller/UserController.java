package com.hhnatsiuk.api_auth_service.impl.controller;

import com.hhnatsiuk.api_auth_if.api.generated.UsersApi;
import com.hhnatsiuk.api_auth_if.model.generated.UserResponseDTO;
import com.hhnatsiuk.auth.api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UsersApi {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or @customSecurityServiceImpl.isCurrentUser(#userUuid)")
    public ResponseEntity<Void> deleteUser(@PathVariable("userUuid") String userUuid) {
        logger.info("Received delete user request for userUuid={}", userUuid);

        userService.deleteUser(userUuid);

        logger.info("User deleted: userUuid={}", userUuid);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ResponseEntity<UserResponseDTO> getUser(String userUuid) {
        logger.info("Received get user request for userUuid={}", userUuid);

        UserResponseDTO userResponseDTO = userService.findUserByUuid(userUuid);

        logger.info("Fetched user: userUuid={}, email={}", userUuid, userResponseDTO.getEmail());

        return ResponseEntity.ok(userResponseDTO);
    }
}
