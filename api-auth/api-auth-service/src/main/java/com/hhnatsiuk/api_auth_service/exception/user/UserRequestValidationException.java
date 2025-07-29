package com.hhnatsiuk.api_auth_service.exception.user;

import com.hhnatsiuk.api_auth_if.model.generated.ErrorDetailDTO;
import com.hhnatsiuk.api_auth_if.model.generated.ValidationErrorResponseAllOfDetailsDTO;
import com.hhnatsiuk.api_auth_service.exception.global.ValidationException;

import java.util.List;

public class UserRequestValidationException extends ValidationException {

    public UserRequestValidationException(String message, List<ErrorDetailDTO> details) {
        super(message, details);
    }
}
