package com.expenshare.repository;

import com.expenshare.model.entity.UserEntity;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UserRepository  extends CrudRepository<UserEntity, Long> {
    boolean existsByEmail(@NonNull String email);
}
