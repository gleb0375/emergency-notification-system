package com.hhnatsiuk.api_auth_service.exception.user;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class UserAlreadyExistsException extends ApiException {

    public UserAlreadyExistsException(int code, String message) {
        super(code, message);
    }
}
