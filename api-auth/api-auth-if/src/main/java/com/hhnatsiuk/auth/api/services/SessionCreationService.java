package com.hhnatsiuk.auth.api.services;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.SessionCreateResponseDTO;

public interface SessionCreationService {

    SessionCreateResponseDTO createSessionForUser(AuthAccountEntity user, String userAgent, String ipAddress);
}
