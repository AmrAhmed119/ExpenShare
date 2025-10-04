package com.expenshare.repository.facade;

import com.expenshare.exception.NotFoundException;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.entity.GroupMemberEntity;
import com.expenshare.repository.GroupMemberRepository;
import com.expenshare.repository.GroupRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.util.List;

@Transactional
@Singleton
public class GroupRepositoryFacade {
    private  final GroupRepository groupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final UserRepositoryFacade userRepositoryFacade;

    public GroupRepositoryFacade(
            GroupRepository groupRepository,
            GroupMemberRepository groupMemberRepository,
            UserRepositoryFacade userRepositoryFacade
    ) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepositoryFacade = userRepositoryFacade;
    }

    public GroupEntity getOrThrow(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Group not found"));
    }

    public boolean usersExist(List<Long> userIds) {
        return userIds.stream()
                .allMatch(userId -> userRepositoryFacade.findById(userId).isPresent());
    }

    public void addMembers(Long groupId, List<Long> userIds) {
        final GroupEntity entity = getOrThrow(groupId);

        userIds.stream()
            .map(userId -> GroupMemberEntity.builder()
                .group(entity)
                .user(userRepositoryFacade.getOrThrow(userId))
                .build())
            .forEach(membership -> {
                Long userId = membership.getUser().getId();
                // Ignore duplicates to maintain unique constraint of (group_id, user_id)
                if (groupMemberRepository.findByGroupIdAndUserId(groupId, userId).isEmpty()) {
                    groupMemberRepository.save(membership);
                    entity.getMembers().add(membership);
                }
            });
    }

    public GroupEntity create(GroupEntity group) {
        return groupRepository.save(group);
    }
}
