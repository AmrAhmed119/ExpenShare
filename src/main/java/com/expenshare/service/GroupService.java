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
import com.expenshare.repository.facade.GroupRepositoryFacade;
import jakarta.inject.Singleton;

/**
 * Service for group-related operations such as creation, retrieval, and member management.
 * Handles persistence and event publishing for groups.
 */
@Singleton
public class GroupService {
    private final GroupRepositoryFacade groupRepositoryFacade;

    private final GroupMapper groupMapper;

    private final KafkaProducer kafkaProducer;

    public GroupService(
        GroupRepositoryFacade groupRepositoryFacade,
        GroupMapper groupMapper,
        KafkaProducer kafkaProducer
    ) {
        this.groupRepositoryFacade = groupRepositoryFacade;
        this.groupMapper = groupMapper;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Creates a new group, persists it, adds members, and publishes related events.
     *
     * @param createGroupRequest the request containing group data and member IDs
     * @return the created group as a DTO
     * @throws NotFoundException if one or more users are not found
     */
    public GroupDto createGroup(CreateGroupRequest createGroupRequest) {
        if (!groupRepositoryFacade.usersExist(createGroupRequest.getMembers())) {
            throw new NotFoundException("One or more users not found");
        }

        final GroupEntity savedGroup = groupRepositoryFacade.create(
                groupMapper.toEntity(createGroupRequest)
        );

        groupRepositoryFacade.addMembers(savedGroup.getId(), createGroupRequest.getMembers());

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
        if (!groupRepositoryFacade.usersExist(addMembersRequest.getMembers())) {
            throw new NotFoundException("One or more users not found");
        }

        groupRepositoryFacade.addMembers(groupId, addMembersRequest.getMembers());

        final GroupEntity updatedGroup = groupRepositoryFacade.getOrThrow(groupId);
        return new AddMembersDto(updatedGroup.getId(), addMembersRequest.getMembers(), updatedGroup.getMembers().size());
    }
}
