package com.hhnatsiuk.api_contact_service.exception.validation;

import com.hhnatsiuk.api_contact_if.model.generated.ErrorDetailDTO;
import com.hhnatsiuk.api_contact_service.exception.global.ValidationException;

import java.util.List;

public class EmptyBodyValidationException extends ValidationException {

    public EmptyBodyValidationException() {
        super("Request body is empty",
                List.of(new ErrorDetailDTO().field("body").error("must not be null/empty")));
    }
}
