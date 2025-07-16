package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_adapter_db.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByRefreshToken(String token);
    List<RefreshTokenEntity> findAllByCredentialsId(Long credentialsId);
}
