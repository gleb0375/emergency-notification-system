package com.hhnatsiuk.api_auth_service.exception.verification;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class VerificationNotFoundException extends ApiException {

    public VerificationNotFoundException(int code, String message) {
        super(code, message);
    }
}
