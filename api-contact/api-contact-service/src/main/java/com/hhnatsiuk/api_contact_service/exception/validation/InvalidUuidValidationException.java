package com.hhnatsiuk.api_contact_service.exception.validation;

import com.hhnatsiuk.api_contact_if.model.generated.ErrorDetailDTO;
import com.hhnatsiuk.api_contact_service.exception.global.ValidationException;

import java.util.List;

public class InvalidUuidValidationException extends ValidationException {
    public InvalidUuidValidationException(String field, String value) {
        super("Invalid UUID format",
                List.of(new ErrorDetailDTO().field(field).error("invalid UUID: " + value)));
    }
}
