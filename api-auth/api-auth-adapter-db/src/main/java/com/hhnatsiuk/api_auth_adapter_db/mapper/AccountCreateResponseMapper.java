package com.hhnatsiuk.api_auth_adapter_db.mapper;

import com.hhnatsiuk.api_auth_core.domain.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.AccountCreateResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountCreateResponseMapper {

    public AccountCreateResponseDTO toDto(AuthAccountEntity account,
                                          int verificationTtlMin,
                                          String message) {
        return new AccountCreateResponseDTO()
                .uuid(account.getUuid())
                .email(account.getEmail())
                .expiresInMinutes(verificationTtlMin)
                .message(message);
    }

}
