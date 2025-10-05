package com.expenshare.service;

import com.expenshare.event.KafkaProducer;
import com.expenshare.event.model.MessageFactory;
import com.expenshare.exception.ConflictException;
import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.model.mapper.UserMapper;
import com.expenshare.repository.facade.UserRepositoryFacade;
import jakarta.inject.Singleton;

/**
 * Service for user-related operations such as creation and retrieval.
 * Handles persistence and event publishing.
 */
@Singleton
public class UserService {
    private final UserRepositoryFacade userRepositoryFacade;

    private final KafkaProducer kafkaProducer;

    private final UserMapper userMapper;

    public UserService(
        UserRepositoryFacade userRepositoryFacade,
        UserMapper userMapper,
        KafkaProducer kafkaProducer
    ) {
        this.userRepositoryFacade = userRepositoryFacade;
        this.userMapper = userMapper;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Creates a new user, persists it, and publishes related events.
     *
     * @param createUserRequest the request containing user data
     * @return the created user as a DTO
     * @throws ConflictException if the email already exists
     */
    public UserDto createUser(CreateUserRequest createUserRequest) {
        final UserEntity entity = userMapper.toEntity(createUserRequest);
        if (userRepositoryFacade.existsByEmail(entity.getEmail().toLowerCase())) {
            throw new ConflictException("Email already exists");
        }

        final UserEntity savedUser = userRepositoryFacade.save(entity);

        kafkaProducer.publishUserCreatedEvent(
                MessageFactory.entityCreatedMessage(entity.getId())
        );
        kafkaProducer.publishWelcomeNotificationEvent(
                MessageFactory.welcomeNotificationMessage("USER", savedUser.getId(), "EMAIL")
        );

        return userMapper.toDto(savedUser);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID
     * @return the user as a DTO
     * @throws com.expenshare.exception.NotFoundException if the user is not found
     */
    public UserDto getUserById(Long id) {
        final UserEntity entity = userRepositoryFacade.getOrThrow(id);
        return userMapper.toDto(entity);
    }
}
