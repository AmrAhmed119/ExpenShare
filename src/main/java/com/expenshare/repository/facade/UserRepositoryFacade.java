package com.expenshare.repository.facade;

import com.expenshare.exception.ConflictException;
import com.expenshare.exception.NotFoundException;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.repository.UserRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Singleton
public class UserRepositoryFacade {
    private final UserRepository userRepository;

    public UserRepositoryFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity getOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public boolean existsByEmail(String emailLower) {
        return userRepository.existsByEmail(emailLower);
    }

    public UserEntity create(UserEntity user) {
        final String emailLower = user.getEmail().toLowerCase();
        if (existsByEmail(emailLower)) {
            throw new ConflictException("Email already exists");
        }

        user.setEmail(emailLower);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

}
