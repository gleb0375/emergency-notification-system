package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SendVerificationRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.VerificationResponseDTO;
import com.hhnatsiuk.auth.api.services.EmailVerificationService;

public class EmailVerificationServiceImpl implements EmailVerificationService {


    @Override
    public VerificationResponseDTO sendVerificationCode(SendVerificationRequestDTO sendVerificationRequestDTO) {
        return null;
    }

    @Override
    public ConfirmEmailResponseDTO confirmEmail(ConfirmEmailRequestDTO request) {
        return null;
    }
}
