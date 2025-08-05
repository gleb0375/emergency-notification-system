package com.hhnatsiuk.api_auth_service.exception.token;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class TokenEntitySaveException extends ApiException {

    public TokenEntitySaveException(int code, String message) {
        super(code, message);
    }
}
