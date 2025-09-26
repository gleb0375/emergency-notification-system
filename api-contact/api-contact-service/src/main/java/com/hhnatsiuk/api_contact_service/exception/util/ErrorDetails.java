package com.hhnatsiuk.api_contact_service.exception.util;

import com.hhnatsiuk.api_contact_if.model.generated.ErrorDetailDTO;

public final class ErrorDetails {
    private ErrorDetails() {}

    public static ErrorDetailDTO of(String field, String message) {
        ErrorDetailDTO d = new ErrorDetailDTO();
        d.setField(field);
        d.setError(message);
        return d;
    }
}
