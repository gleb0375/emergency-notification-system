package com.hhnatsiuk.api_auth_service.impl.controller;

import com.hhnatsiuk.api_auth_if.api.generated.SessionsApi;
import com.hhnatsiuk.api_auth_if.model.generated.SignInRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.SignInResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController implements SessionsApi {

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);


    @Override
    public ResponseEntity<SignInResponseDTO> signIn(SignInRequestDTO dto) {


        return null;
    }

}
