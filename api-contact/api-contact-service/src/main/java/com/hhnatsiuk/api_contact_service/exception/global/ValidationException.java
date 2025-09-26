package com.hhnatsiuk.api_contact_service.exception.global;

import com.hhnatsiuk.api_contact_if.model.generated.ErrorDetailDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ValidationException extends ApiException {

    @Getter private final List<ErrorDetailDTO> details;

    public ValidationException(String message, List<ErrorDetailDTO> details) {
        super(HttpStatus.UNPROCESSABLE_ENTITY.value(), message);
        this.details = details;
    }
    public ValidationException(String message) {
        this(message, null);
    }

}

