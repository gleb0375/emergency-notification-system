package com.hhnatsiuk.api_auth_service.exception.token;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class SessionNotFoundException extends ApiException {

    public SessionNotFoundException(int code, String message) {
        super(code, message);
    }
}
