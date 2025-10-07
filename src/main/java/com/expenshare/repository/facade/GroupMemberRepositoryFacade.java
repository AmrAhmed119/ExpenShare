package com.expenshare.repository.facade;

import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.GroupMemberEntity;
import com.expenshare.repository.GroupMemberRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.util.List;

@Transactional
@Singleton
public class GroupMemberRepositoryFacade {
    private final GroupMemberRepository groupMemberRepository;

    private final GroupRepositoryFacade groupRepositoryFacade;

    private final UserRepositoryFacade userRepositoryFacade;

    public GroupMemberRepositoryFacade(
        GroupMemberRepository groupMemberRepository,
        GroupRepositoryFacade groupRepositoryFacade,
        UserRepositoryFacade userRepositoryFacade
    ) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupRepositoryFacade = groupRepositoryFacade;
        this.userRepositoryFacade = userRepositoryFacade;
    }

    public boolean existsByGroupIdAndUserId(Long groupId, Long userId) {
        return groupMemberRepository.existsByGroupIdAndUserId(groupId, userId);
    }

    public List<GroupMemberEntity> findAllByGroupId(Long groupId) {
        return groupMemberRepository.findAllByGroupId(groupId);
    }

    public List<Long> findGroupMemberIds(Long groupId) {
        return findAllByGroupId(groupId)
                .stream()
                .map(groupMember -> groupMember.getUser().getId())
                .toList();
    }

    public void saveMembers(Long groupId, List<Long> userIds) {
        final GroupEntity entity = groupRepositoryFacade.getOrThrow(groupId);

        userIds.stream()
                .map(userId -> GroupMemberEntity.builder()
                        .group(entity)
                        .user(userRepositoryFacade.getOrThrow(userId))
                        .build())
                .forEach(membership -> {
                    Long userId = membership.getUser().getId();
                    // Ignore duplicates to maintain unique constraint of (group_id, user_id)
                    if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
                        groupMemberRepository.save(membership);
                        entity.getMembers().add(membership);
                    }
                });
    }
}
