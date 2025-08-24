package com.hhnatsiuk.api_auth_adapter_db.mapper;

import com.hhnatsiuk.api_auth_core.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_if.model.generated.UserResponseDTO;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Component
public class UserResponseMapper {

    public UserResponseDTO toDto(AuthAccountEntity account) {
        if (account == null) return null;

        return new UserResponseDTO()
                .uuid(account.getUuid())
                .email(account.getEmail())
                .roles(account.getRoleNames())
                .verified(account.isEmailVerified())
                .createdAt(toOffset(account.getCreatedAt()))
                .updatedAt(toOffset(account.getUpdatedAt()));
    }

    public List<UserResponseDTO> toDtoList(List<AuthAccountEntity> accounts) {
        if (accounts == null || accounts.isEmpty()) return List.of();
        return accounts.stream()
                .filter(Objects::nonNull)
                .map(this::toDto)
                .toList();
    }

    private OffsetDateTime toOffset(java.time.LocalDateTime ldt) {
        return ldt == null ? null : ldt.atOffset(ZoneOffset.UTC);
    }
}
