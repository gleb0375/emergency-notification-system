package com.hhnatsiuk.api_auth_service.exception.user;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class UserAccessDeniedException extends ApiException {

    public UserAccessDeniedException(int code, String message) {
        super(code, message);
    }
}
