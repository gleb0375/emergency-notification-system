package com.hhnatsiuk.api_auth_service.exception.session;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class SessionAccessDeniedException extends ApiException {

    public SessionAccessDeniedException(int code, String message) {
        super(code, message);
    }
}
