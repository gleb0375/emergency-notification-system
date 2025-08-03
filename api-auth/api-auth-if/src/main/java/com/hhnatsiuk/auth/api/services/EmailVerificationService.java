package com.hhnatsiuk.auth.api.services;

import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.ConfirmEmailResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SendVerificationRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.VerificationResponseDTO;

public interface EmailVerificationService {

    VerificationResponseDTO sendVerificationCode(SendVerificationRequestDTO sendVerificationRequestDTO);

    ConfirmEmailResponseDTO confirmEmail(String token, ConfirmEmailRequestDTO request);

}
