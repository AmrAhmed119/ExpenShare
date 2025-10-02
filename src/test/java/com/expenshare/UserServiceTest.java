package com.expenshare;

import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.service.UserService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false, environments = "test")
public class UserServiceTest {
    @Inject
    UserService userService;

    @Test
    void testCreateUserSuccess() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setMobileNumber("123456789");

        UserDto created = userService.createUser(request);

        assertNotNull(created.getId());
        assertEquals("john.doe@example.com", created.getEmail());

        UserDto fetched = userService.getUserById(created.getId());
        assertEquals(created.getId(), fetched.getId());
        assertEquals("John Doe", fetched.getName());
    }

    @Test
    void testGetUserNotFound() {
        assertThrows(
                com.expenshare.exception.NotFoundException.class,
                () -> userService.getUserById(9999L)
        );
    }

    @Test
    void testDuplicateEmailThrowsConflict() {
        CreateUserRequest req1 = new CreateUserRequest();
        req1.setName("Alice");
        req1.setEmail("alice@example.com");
        userService.createUser(req1);

        CreateUserRequest req2 = new CreateUserRequest();
        req2.setName("Another Alice");
        req2.setEmail("alice@example.com");

        assertThrows(
                com.expenshare.exception.ConflictException.class,
                () -> userService.createUser(req2)
        );
    }
}
