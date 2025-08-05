package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_core.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByRefreshToken(String token);

    Optional<TokenEntity> findFirstByAccount_IdAndExpiresAtAfterOrderByExpiresAtDesc(Long accountId, Timestamp now);
}
