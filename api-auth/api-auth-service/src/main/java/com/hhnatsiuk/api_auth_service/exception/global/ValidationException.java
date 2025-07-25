package com.hhnatsiuk.api_auth_service.exception.global;

import com.hhnatsiuk.api_auth_if.model.generated.ValidationErrorResponseAllOfDetailsDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private final int code;
    private final List<ValidationErrorResponseAllOfDetailsDTO> details;

    public ValidationException(int code,
                               String message,
                               List<ValidationErrorResponseAllOfDetailsDTO> details) {
        super(message);
        this.code = code;
        this.details = details;
    }

    public ValidationException(int code,
                               String message,
                               List<ValidationErrorResponseAllOfDetailsDTO> details,
                               Throwable cause) {
        super(message, cause);
        this.code = code;
        this.details = details;
    }
}
