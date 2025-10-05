package com.expenshare.service;

import com.expenshare.event.KafkaProducer;
import com.expenshare.event.model.MessageFactory;
import com.expenshare.exception.NotFoundException;
import com.expenshare.model.dto.group.AddMembersDto;
import com.expenshare.model.dto.group.AddMembersRequest;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.model.mapper.GroupMapper;
import com.expenshare.repository.facade.GroupMemberRepositoryFacade;
import com.expenshare.repository.facade.GroupRepositoryFacade;
import com.expenshare.repository.facade.UserRepositoryFacade;
import jakarta.inject.Singleton;

/**
 * Service for group-related operations such as creation, retrieval, and member management.
 * Handles persistence and event publishing for groups.
 */
@Singleton
public class GroupService {
    private final GroupRepositoryFacade groupRepositoryFacade;

    private final UserRepositoryFacade userRepositoryFacade;

    private final GroupMapper groupMapper;

    private final KafkaProducer kafkaProducer;

    private final GroupMemberRepositoryFacade groupMemberRepositoryFacade;

    public GroupService(
        GroupRepositoryFacade groupRepositoryFacade,
        GroupMapper groupMapper,
        KafkaProducer kafkaProducer,
        UserRepositoryFacade userRepositoryFacade,
        GroupMemberRepositoryFacade groupMemberRepositoryFacade
    ) {
        this.groupRepositoryFacade = groupRepositoryFacade;
        this.groupMapper = groupMapper;
        this.kafkaProducer = kafkaProducer;
        this.userRepositoryFacade = userRepositoryFacade;
        this.groupMemberRepositoryFacade = groupMemberRepositoryFacade;
    }

    /**
     * Creates a new group, persists it, adds members, and publishes related events.
     *
     * @param createGroupRequest the request containing group data and member IDs
     * @return the created group as a DTO
     * @throws NotFoundException if one or more users are not found
     */
    public GroupDto createGroup(CreateGroupRequest createGroupRequest) {
        if (!userRepositoryFacade.isUsersExist(createGroupRequest.getMembers())) {
            throw new NotFoundException("One or more users not found");
        }

        final GroupEntity savedGroup = groupRepositoryFacade.create(
                groupMapper.toEntity(createGroupRequest)
        );

        groupMemberRepositoryFacade.saveMembers(savedGroup.getId(), createGroupRequest.getMembers());

        kafkaProducer.publishGroupCreatedEvent(
                MessageFactory.entityCreatedMessage(savedGroup.getId())
        );
        kafkaProducer.publishWelcomeNotificationEvent(
                MessageFactory.welcomeNotificationMessage("GROUP", savedGroup.getId(), "EMAIL")
        );

        final GroupEntity updatedGroup = groupRepositoryFacade.getOrThrow(savedGroup.getId());
        return groupMapper.toDto(updatedGroup, updatedGroup.getMemberIds());
    }

    /**
     * Retrieves a group by ID.
     *
     * @param id the group ID
     * @return the group as a DTO
     * @throws NotFoundException if the group is not found
     */
    public GroupDto getGroupById(Long id) {
        final GroupEntity entity = groupRepositoryFacade.getOrThrow(id);
        return groupMapper.toDto(entity, entity.getMemberIds());
    }

    /**
     * Adds members to an existing group.
     * <p>
     * Duplicate user IDs in the request are ignored.
     * Users already present in the group are also ignored, maintaining the uniqueness constraint for (user\_id, group\_id) pairs.
     * </p>
     *
     * @param groupId the group ID
     * @param addMembersRequest the request containing member IDs to add
     * @return DTO containing group ID, added member IDs, and total member count
     * @throws NotFoundException if one or more users are not found, or the group does not exist
     */
    public AddMembersDto addMembersToGroup(Long groupId, AddMembersRequest addMembersRequest) {
        if (!userRepositoryFacade.isUsersExist(addMembersRequest.getMembers())) {
            throw new NotFoundException("One or more users not found");
        }

        groupMemberRepositoryFacade.saveMembers(groupId, addMembersRequest.getMembers());

        final GroupEntity updatedGroup = groupRepositoryFacade.getOrThrow(groupId);
        return new AddMembersDto(updatedGroup.getId(), addMembersRequest.getMembers(), updatedGroup.getMembers().size());
    }
}
