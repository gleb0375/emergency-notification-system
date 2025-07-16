package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_adapter_db.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CredentialRepository extends JpaRepository<CredentialEntity, Long> {
    Optional<CredentialEntity> findByEmail(String email);
}
