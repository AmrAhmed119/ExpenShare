package com.expenshare.controller;

import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;

@Controller("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Post
    public HttpResponse<UserDto> createUser(@Body @Valid CreateUserRequest createUserRequest) {
        final UserDto savedUser = userService.createUser(createUserRequest);
        return HttpResponse.created(savedUser);
    }

    @Get("/{userId}")
    public HttpResponse<UserDto> getUserById(@PathVariable Long userId) {
        final UserDto user = userService.getUserById(userId);
        return HttpResponse.ok(user);
    }
}
