package com.hhnatsiuk.api_auth_service.validation;

import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_service.exception.user.UserRequestValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserValidator {

    private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

//    public static void validateUserForm(AccountCreateRequestDTO userCreateRequestDTO) {
//        if (userCreateRequestDTO == null) {
//            logger.error("User request is null");
//            throw new UserRequestValidationException("User can not be null!",
//                    List.of(createError("user", "")));;
//        }
//        logger.debug("Validating user form: {}", userCreateRequestDTO);
//        validateUserForm(userCreateRequestDTO.getFirstName(), userCreateRequestDTO.getLastName(), userCreateRequestDTO.getEmail(), userCreateRequestDTO.getPhone());
//    }



}
