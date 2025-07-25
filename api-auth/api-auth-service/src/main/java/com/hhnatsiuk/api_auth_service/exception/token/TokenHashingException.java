package com.hhnatsiuk.api_auth_service.exception.token;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class TokenHashingException extends ApiException {

    public TokenHashingException(int code, String message) {
        super(code, message);
    }

}
