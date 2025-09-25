package com.hhnatsiuk.api_contact_adapter_db.repository;

import com.hhnatsiuk.api_contact_core.domain.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {

    Optional<UserProfileEntity> findByUuid(String uuid);

    Optional<UserProfileEntity> findByAccountUuid(String accountUuid);
    Optional<UserProfileEntity> findByAuthAccountUuid(String authAccountUuid);

    Optional<UserProfileEntity> findByEmail(String email);

    Optional<UserProfileEntity> findByTelegramUsername(String telegramUsername);

    Optional<UserProfileEntity> findByPhoneNumber(String phoneNumber);

    boolean existsByAccountUuid(String accountUuid);

    boolean existsByEmail(String email);
}
