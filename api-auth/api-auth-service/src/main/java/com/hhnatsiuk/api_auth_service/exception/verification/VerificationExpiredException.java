package com.hhnatsiuk.api_auth_service.exception.verification;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class VerificationExpiredException extends ApiException {
    public VerificationExpiredException(int code, String message) {
        super(code, message);
    }
}
