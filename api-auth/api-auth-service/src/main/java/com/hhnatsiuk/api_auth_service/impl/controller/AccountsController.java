package com.hhnatsiuk.api_auth_service.impl.controller;

import com.hhnatsiuk.api_auth_if.api.generated.AccountsApi;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import org.springframework.http.ResponseEntity;

public class AccountsController implements AccountsApi {


    @Override
    public ResponseEntity<AccountCreateResponseDTO> registerAccount(AccountCreateRequestDTO userCreateRequestDTO) {
        return null;
    }

}
