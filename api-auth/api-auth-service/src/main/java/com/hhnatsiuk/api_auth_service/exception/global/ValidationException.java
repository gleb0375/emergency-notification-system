package com.hhnatsiuk.api_auth_service.exception.global;

import com.hhnatsiuk.api_auth_if.model.generated.ErrorDetailDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private final List<ErrorDetailDTO> details;

    public ValidationException(String message,
                               List<ErrorDetailDTO> details) {
        super(message);
        this.details = details;
    }

    public ValidationException(String message,
                               List<ErrorDetailDTO> details,
                               Throwable cause) {
        super(message, cause);
        this.details = details;
    }
}
