package com.expenshare.service;

import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.mapper.UserMapper;
import com.expenshare.repository.facade.UserRepositoryFacade;
import jakarta.inject.Singleton;

@Singleton
public class UserService {
    private final UserRepositoryFacade userRepositoryFacade;

    private final UserMapper userMapper;

    public UserService(UserRepositoryFacade userRepositoryFacade, UserMapper userMapper) {
        this.userRepositoryFacade = userRepositoryFacade;
        this.userMapper = userMapper;
    }

    public UserDto createUser(CreateUserRequest createUserRequest) {
        final UserEntity entity = userMapper.toEntity(createUserRequest);
        final UserEntity savedUser = userRepositoryFacade.create(entity);
        // TODO: publish events to kafka
        return userMapper.toDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        final UserEntity entity = userRepositoryFacade.getOrThrow(id);
        return userMapper.toDto(entity);
    }
}
