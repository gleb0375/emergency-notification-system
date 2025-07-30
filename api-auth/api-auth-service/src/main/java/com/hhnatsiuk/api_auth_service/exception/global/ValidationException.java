package com.hhnatsiuk.api_auth_service.exception.global;

import com.hhnatsiuk.api_auth_if.model.generated.ErrorDetailDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ValidationException extends RuntimeException {
    @Getter
    private final int code;
    private final String message;
    @Getter
    private final List<ErrorDetailDTO> details;

    public ValidationException(String message, List<ErrorDetailDTO> details) {
        super(message);
        this.message = message;
        this.code = HttpStatus.UNPROCESSABLE_ENTITY.value();
        this.details = details;
    }
}
