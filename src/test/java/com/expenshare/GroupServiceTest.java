package com.expenshare;

import com.expenshare.model.dto.group.AddMembersDto;
import com.expenshare.model.dto.group.AddMembersRequest;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.service.GroupService;
import com.expenshare.exception.NotFoundException;
import com.expenshare.service.UserService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false, environments = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Enables non-static @BeforeAll
public class GroupServiceTest {
    @Inject
    GroupService groupService;

    @Inject
    UserService userService;

    @BeforeAll
    void setupUsers() {
        for (int i = 1; i <= 10; i++) {
            CreateUserRequest req = new CreateUserRequest();
            req.setName("User" + i);
            req.setEmail("user" + i + "@example.com");
            req.setMobileNumber("+12345678" + i);
            userService.createUser(req);
        }
    }

    @Test
    void testCreateGroupSuccess() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("Trip to Dubai");
        request.setMembers(Arrays.asList(1L, 2L, 3L));

        GroupDto created = groupService.createGroup(request);

        assertNotNull(created.getGroupId());
        assertEquals("Trip to Dubai", created.getName());
        assertEquals(3, created.getMembers().size());
    }

    @Test
    void testCreateGroupUserNotFound() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("Invalid Group");
        request.setMembers(List.of(9999L));

        assertThrows(NotFoundException.class, () -> groupService.createGroup(request));
    }

    @Test
    void testGetGroupByIdSuccess() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("Friends");
        request.setMembers(Arrays.asList(1L, 2L));
        GroupDto created = groupService.createGroup(request);

        GroupDto fetched = groupService.getGroupById(created.getGroupId());
        assertEquals(created.getGroupId(), fetched.getGroupId());
        assertEquals("Friends", fetched.getName());
    }

    @Test
    void testGetGroupByIdNotFound() {
        assertThrows(NotFoundException.class, () -> groupService.getGroupById(9999L));
    }

    @Test
    void testAddMembersToGroupSuccess() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("Colleagues");
        request.setMembers(Arrays.asList(1L, 2L));
        GroupDto group = groupService.createGroup(request);

        AddMembersRequest addRequest = new AddMembersRequest();
        addRequest.setMembers(Arrays.asList(3L, 4L));

        AddMembersDto result = groupService.addMembersToGroup(group.getGroupId(), addRequest);

        assertEquals(group.getGroupId(), result.getGroupId());
        assertEquals(Arrays.asList(3L, 4L), result.getMembersAdded());
        assertEquals(4, result.getTotalMembers());
    }

    @Test
    void testAddMembersToGroupUserNotFound() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("Test Group");
        request.setMembers(List.of(1L));
        GroupDto group = groupService.createGroup(request);

        AddMembersRequest addRequest = new AddMembersRequest();
        addRequest.setMembers(List.of(9999L));

        assertThrows(NotFoundException.class, () -> groupService.addMembersToGroup(group.getGroupId(), addRequest));
    }

    @Test
    void testAddMembersToGroupAlreadyExistingMembers() {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setName("Existing Members Group");
        request.setMembers(Arrays.asList(1L, 2L));
        GroupDto group = groupService.createGroup(request);

        AddMembersRequest addRequest = new AddMembersRequest();
        addRequest.setMembers(Arrays.asList(1L, 2L, 3L));

        AddMembersDto result = groupService.addMembersToGroup(group.getGroupId(), addRequest);

        assertEquals(group.getGroupId(), result.getGroupId());
        assertEquals(3, result.getTotalMembers());
    }

    @Test
    void testAddMembersToGroupGroupNotFound() {
        AddMembersRequest addRequest = new AddMembersRequest();
        addRequest.setMembers(List.of(1L));

        assertThrows(NotFoundException.class, () -> groupService.addMembersToGroup(9999L, addRequest));
    }
}
