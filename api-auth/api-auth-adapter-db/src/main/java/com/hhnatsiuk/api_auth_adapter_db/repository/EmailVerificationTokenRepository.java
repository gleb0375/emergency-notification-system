package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_adapter_db.entity.EmailVerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationTokenEntity, Long> {
    Optional<EmailVerificationTokenEntity> findByToken(String token);
    List<EmailVerificationTokenEntity> findAllByCredentialsId(Long credentialsId);
}
