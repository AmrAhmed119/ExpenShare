package com.expenshare.repository;

import com.expenshare.model.entity.GroupMemberEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface GroupMemberRepository extends CrudRepository<GroupMemberEntity, Long> {
    Optional<GroupMemberEntity> findByGroupIdAndUserId(Long groupId, Long userId);
}
