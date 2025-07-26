package com.hhnatsiuk.auth.api.services;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.UserResponseDTO;

import java.util.List;

public interface UserService {

    AccountCreateResponseDTO createUser(AccountCreateRequestDTO userCreateRequestDTO);

    UserResponseDTO findUserByUuid(String uuid);

    List<UserResponseDTO> findAllUsers();

    void deleteUser(String uuid);

    AuthAccountEntity findUserByEmail(String email);
}
