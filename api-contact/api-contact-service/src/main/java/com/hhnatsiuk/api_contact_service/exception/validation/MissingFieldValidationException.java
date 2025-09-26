package com.hhnatsiuk.api_contact_service.exception.validation;

import com.hhnatsiuk.api_contact_if.model.generated.ErrorDetailDTO;
import com.hhnatsiuk.api_contact_service.exception.global.ValidationException;

import java.util.List;

public class MissingFieldValidationException extends ValidationException {

    public MissingFieldValidationException(String field) {
        super("Missing required field: " + field,
                List.of(new ErrorDetailDTO().field(field).error("must be provided")));
    }
}
