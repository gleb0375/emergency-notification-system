package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_core.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<SessionEntity, Long> {
    Optional<SessionEntity> findByRefreshToken(String token);
    //List<SessionEntity> findAllByCredentialsId(Long credentialsId);
}
