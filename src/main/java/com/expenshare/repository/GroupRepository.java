package com.expenshare.repository;

import com.expenshare.model.entity.GroupEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
}
