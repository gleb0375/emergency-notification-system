package com.hhnatsiuk.api_auth_service.exception.token;

import com.hhnatsiuk.api_auth_service.exception.global.ApiException;

public class EmailSendingException extends ApiException {

    public EmailSendingException(int code, String message) {
        super(code, message);
    }
}
