package com.hhnatsiuk.api_contact_adapter_db.repository;

import com.hhnatsiuk.api_contact_core.domain.entity.GroupMembershipEntity;
import com.hhnatsiuk.api_contact_core.domain.entity.embeddable.GroupMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembershipEntity, GroupMembershipId> {

    List<GroupMembershipEntity> findAllByIdGroupId(Integer groupId);

    List<GroupMembershipEntity> findAllByIdProfileId(Long profileId);

    boolean existsByIdGroupIdAndIdProfileId(Integer groupId, Long profileId);

    long deleteByIdGroupIdAndIdProfileId(Integer groupId, Long profileId);
}
