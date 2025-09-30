package com.expenshare.controller;

import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;

@Controller("/api/users")
public class UserController {

    @Post
    public HttpResponse<UserDto> createUser(@Body @Valid CreateUserRequest createUserRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Get("/{userId}")
    public HttpResponse<UserDto> getUser(@PathVariable Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
