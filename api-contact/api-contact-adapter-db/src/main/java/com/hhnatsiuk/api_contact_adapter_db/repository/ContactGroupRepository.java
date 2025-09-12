package com.hhnatsiuk.api_contact_adapter_db.repository;

import com.hhnatsiuk.api_contact_core.domain.entity.ContactGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactGroupRepository extends JpaRepository<ContactGroupEntity, Integer> {
}
