package com.hhnatsiuk.api_auth_service.exception.user;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class RoleNotFoundException extends ApiException {

    public RoleNotFoundException(int code, String message) {
        super(code, message);
    }
}
