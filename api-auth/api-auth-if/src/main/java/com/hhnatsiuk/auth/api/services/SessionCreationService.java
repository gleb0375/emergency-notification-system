package com.hhnatsiuk.auth.api.services;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SignInResponseDTO;

public interface SessionCreationService {

    SignInResponseDTO createSessionForUser(AuthAccountEntity user);
}
