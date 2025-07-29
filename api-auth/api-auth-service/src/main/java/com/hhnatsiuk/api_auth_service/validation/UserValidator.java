package com.hhnatsiuk.api_auth_service.validation;

import com.hhnatsiuk.api_auth_adapter_db.repository.AuthAccountRepository;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.ErrorDetailDTO;
import com.hhnatsiuk.api_auth_service.exception.user.UserAlreadyExistsException;
import com.hhnatsiuk.api_auth_service.exception.user.UserRequestValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {

    private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

    private static final String EMAIL_FIELD    = "email";
    private static final String PASSWORD_FIELD = "password";


    public static void validateForCreate(AccountCreateRequestDTO dto,
                                         AuthAccountRepository repo) {
        validateUserForm(dto);
        validatePassword(dto);
        validateUniqueEmail(dto, repo);
    }

    private static void validateUserForm(AccountCreateRequestDTO dto) {
        List<ErrorDetailDTO> errors = new ArrayList<>();
        if (dto == null) {
            logger.error("AccountCreateRequestDTO is null");
            throw new UserRequestValidationException(
                    "Request body must not be null",
                    List.of(createError("user", "Request body is required"))
            );
        }

        String email = dto.getEmail();
        if (email == null || email.isBlank()) {
            errors.add(createError(EMAIL_FIELD, "Email is required"));
        } else if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errors.add(createError(EMAIL_FIELD, "Email format is invalid"));
        }

        if (!errors.isEmpty()) {
            logger.warn("User form validation failed: {}", errors);
            throw new UserRequestValidationException(
                    "Validation failed for user data",
                    errors
            );
        }
    }

    private static void validatePassword(AccountCreateRequestDTO dto) {
        List<ErrorDetailDTO> errors = new ArrayList<>();
        String pwd = dto.getPassword();

        if (pwd == null || pwd.isBlank()) {
            errors.add(createError(PASSWORD_FIELD, "Password is required"));
        } else {
            if (pwd.length() < 8 || pwd.length() > 20) {
                errors.add(createError(PASSWORD_FIELD, "Password must be 8–20 characters long"));
            }
            if (!pwd.matches(".*[A-Z].*")) {
                errors.add(createError(PASSWORD_FIELD, "Must contain at least one uppercase letter"));
            }
            if (!pwd.matches(".*[a-z].*")) {
                errors.add(createError(PASSWORD_FIELD, "Must contain at least one lowercase letter"));
            }
            if (!pwd.matches(".*\\d.*")) {
                errors.add(createError(PASSWORD_FIELD, "Must contain at least one digit"));
            }
        }

        if (!errors.isEmpty()) {
            logger.warn("Password validation failed: {}", errors);
            throw new UserRequestValidationException(
                    "Validation failed for password",
                    errors
            );
        }
    }

    private static void validateUniqueEmail(AccountCreateRequestDTO dto,
                                            AuthAccountRepository repo) {
        String email = dto.getEmail().trim().toLowerCase();
        if (repo.existsByEmail(email)) {
            logger.warn("Email already in use: {}", email);
            throw new UserAlreadyExistsException(
                    409,
                    "An account with this email already exists"
            );
        }
    }

    private static ErrorDetailDTO createError(String field, String message) {
        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setField(field);
        detail.setError(message);
        return detail;
    }
}
