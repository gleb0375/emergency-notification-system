package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_core.domain.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByUuid(String token);

    Optional<TokenEntity> findFirstByAccount_IdAndExpiresAtAfterOrderByExpiresAtDesc(Long accountId, LocalDateTime now);
}
