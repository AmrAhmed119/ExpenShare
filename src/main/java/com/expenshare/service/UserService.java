package com.expenshare.service;

import com.expenshare.event.KafkaProducer;
import com.expenshare.event.model.MessageFactory;
import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.mapper.UserMapper;
import com.expenshare.repository.facade.UserRepositoryFacade;
import jakarta.inject.Singleton;

@Singleton
public class UserService {
    private final UserRepositoryFacade userRepositoryFacade;

    private final KafkaProducer kafkaProducer;

    private final UserMapper userMapper;

    public UserService(UserRepositoryFacade userRepositoryFacade, UserMapper userMapper, KafkaProducer kafkaProducer) {
        this.userRepositoryFacade = userRepositoryFacade;
        this.userMapper = userMapper;
        this.kafkaProducer = kafkaProducer;
    }

    public UserDto createUser(CreateUserRequest createUserRequest) {
        final UserEntity entity = userMapper.toEntity(createUserRequest);
        final UserEntity savedUser = userRepositoryFacade.create(entity);

        kafkaProducer.publishUserCreatedEvent(
                MessageFactory.userCreatedMessage(entity.getId())
        );
        kafkaProducer.publishWelcomeNotificationEvent(
                MessageFactory.welcomeNotificationMessage("USER")
        );

        return userMapper.toDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        final UserEntity entity = userRepositoryFacade.getOrThrow(id);
        return userMapper.toDto(entity);
    }
}
