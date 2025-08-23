package com.hhnatsiuk.api_auth_service.exception.session;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class RefreshTokenExpiredException extends ApiException {

    public RefreshTokenExpiredException(int code, String message) {
        super(code, message);
    }
}
