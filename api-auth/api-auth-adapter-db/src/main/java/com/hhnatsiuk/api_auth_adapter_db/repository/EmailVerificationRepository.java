package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_core.domain.entity.AuthAccountEntity;
import com.hhnatsiuk.api_auth_core.domain.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {

    Optional<EmailVerificationEntity> findTopByAccountOrderByCreatedAtDesc(AuthAccountEntity account);
    void deleteAllByAccount(AuthAccountEntity account);

}
