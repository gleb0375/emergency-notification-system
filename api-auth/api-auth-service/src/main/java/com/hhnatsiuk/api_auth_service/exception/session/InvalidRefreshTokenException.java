package com.hhnatsiuk.api_auth_service.exception.session;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class InvalidRefreshTokenException extends ApiException {

    public InvalidRefreshTokenException(int code, String message) {
        super(code, message);
    }
}
