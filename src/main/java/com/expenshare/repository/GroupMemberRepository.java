package com.expenshare.repository;

import com.expenshare.model.entity.GroupMemberEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends CrudRepository<GroupMemberEntity, Long> {
    boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    List<GroupMemberEntity> findAllByGroupId(Long groupId);
}
