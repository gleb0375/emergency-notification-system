package com.hhnatsiuk.api_auth_service.exception.global;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private final int code;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
