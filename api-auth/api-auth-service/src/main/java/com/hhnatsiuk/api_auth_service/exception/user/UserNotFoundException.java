package com.hhnatsiuk.api_auth_service.exception.user;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException(int code, String message) {
        super(code, message);
    }
}
