package com.expenshare.repository.facade;

import com.expenshare.exception.NotFoundException;
import com.expenshare.model.entity.GroupEntity;
import com.expenshare.repository.GroupRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

@Transactional
@Singleton
public class GroupRepositoryFacade {
    private  final GroupRepository groupRepository;

    public GroupRepositoryFacade(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public boolean existsById(Long id) {
        return groupRepository.existsById(id);
    }

    public GroupEntity getOrThrow(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Group not found"));
    }

    public GroupEntity create(GroupEntity group) {
        return groupRepository.save(group);
    }
}
