package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_adapter_db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByCredentialsId(Long credentialsId);
}