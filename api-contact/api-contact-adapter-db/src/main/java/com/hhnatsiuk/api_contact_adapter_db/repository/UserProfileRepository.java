package com.hhnatsiuk.api_contact_adapter_db.repository;

import com.hhnatsiuk.api_contact_core.domain.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {


    Optional<UserProfileEntity> findByAuthAccountUuid(String authAccountUuid);

    Optional<UserProfileEntity> findByUserProfileUuid(String userProfileUuid);
    Optional<UserProfileEntity> findByEmail(String email);

    Optional<UserProfileEntity> findByTelegramUsername(String telegramUsername);

    Optional<UserProfileEntity> findByPhoneNumber(String phoneNumber);
    boolean existsByAuthAccountUuid(String authAccountUuid);

    boolean existsByEmail(String email);
}
