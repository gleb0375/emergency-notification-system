package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_core.domain.entity.AuthAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthAccountRepository extends JpaRepository<AuthAccountEntity, Long>, JpaSpecificationExecutor<AuthAccountEntity> {
    List<AuthAccountEntity> findAuthAccountEntityByEmail(String email);
    Optional<AuthAccountEntity> findByEmail(String email);
    Optional<AuthAccountEntity> findAuthAccountEntityByUuid(String uuid);
    boolean existsByEmail(String email);
}
