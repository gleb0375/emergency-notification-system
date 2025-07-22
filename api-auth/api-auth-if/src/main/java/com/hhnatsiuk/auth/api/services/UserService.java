package com.hhnatsiuk.auth.api.services;

import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.UserResponseDTO;

public interface UserService {

    AccountCreateResponseDTO createUser(AccountCreateRequestDTO userCreateRequestDTO);

    UserResponseDTO findUserByUuid(String uuid);

    void deleteUser(String uuid);
}
