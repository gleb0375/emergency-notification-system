package com.hhnatsiuk.api_contact_service.exception.profile;

import com.hhnatsiuk.api_contact_service.exception.global.ApiException;

public class ProfileNotFoundException extends ApiException {

    public ProfileNotFoundException(int code, String message) {
        super(code, message);
    }
}
