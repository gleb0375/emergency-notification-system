package com.hhnatsiuk.api_auth_adapter_db.repository;

import com.hhnatsiuk.api_auth_adapter_db.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleEntity.Key> {
    List<UserRoleEntity> findAllByCredentialsId(Long credentialsId);
    List<UserRoleEntity> findAllByRoleId(Long roleId);
}
