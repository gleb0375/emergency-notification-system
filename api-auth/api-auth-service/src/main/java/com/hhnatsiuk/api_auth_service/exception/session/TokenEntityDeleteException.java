package com.hhnatsiuk.api_auth_service.exception.session;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class TokenEntityDeleteException extends ApiException {

    public TokenEntityDeleteException(int code, String message) {
        super(code, message);
    }
}
