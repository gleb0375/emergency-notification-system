package com.hhnatsiuk.api_auth_service.impl.service;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateRequestDTO;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import com.hhnatsiuk.api_auth_if.model.generated.UserResponseDTO;
import com.hhnatsiuk.auth.api.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public AccountCreateResponseDTO createUser(AccountCreateRequestDTO userCreateRequestDTO) {
        return null;
    }

    @Override
    public UserResponseDTO findUserByUuid(String uuid) {
        return null;
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(String uuid) {

    }

    @Override
    public AuthAccountEntity findUserByEmail(String email) {
        return null;
    }
}
