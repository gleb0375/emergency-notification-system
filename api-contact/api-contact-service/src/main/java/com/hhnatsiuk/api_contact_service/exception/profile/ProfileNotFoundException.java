package com.hhnatsiuk.api_contact_service.exception.profile;

import com.hhnatsiuk.api_contact_service.exception.global.ApiException;
import org.springframework.http.HttpStatus;

public class ProfileNotFoundException extends ApiException {

    public ProfileNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
