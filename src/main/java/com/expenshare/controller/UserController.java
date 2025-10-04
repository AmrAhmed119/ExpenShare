package com.expenshare.controller;

import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@Controller("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with the provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class),
                examples = @ExampleObject(
                    value = "{ \"userId\": 1, \"name\": \"Amr Ahmed\", \"email\": \"amr@example.com\", \"mobileNumber\": \"+201234567890\", \"address\": { \"line1\": \"123 King Fahd Rd\", \"city\": \"Riyadh\", \"postalCode\": \"11564\", \"country\": \"SA\" }, \"createdAt\": \"2025-09-27T15:00:00Z\" }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email already exists",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"errorCode\": \"CONFLICT\", \"message\": \"Email already exists\" }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"errorCode\": \"BAD_REQUEST\", \"message\": \"Invalid email or mobileNumber\" }"
                )
            )
        )
    })
    @Post
    public HttpResponse<UserDto> createUser(@Body @Valid CreateUserRequest createUserRequest) {
        final UserDto savedUser = userService.createUser(createUserRequest);
        return HttpResponse.created(savedUser);
    }

    @Operation(
        summary = "Get user by ID",
        description = "Retrieves a user by their unique ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class),
                examples = @ExampleObject(
                    value = "{ \"id\": 1, \"email\": \"john.doe@example.com\", \"name\": \"John Doe\", \"createdAt\": \"2024-06-01T12:34:56\" }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"errorCode\": \"NOT_FOUND\", \"message\": \"User not found\" }"
                )
            )
        )
    })
    @Get("/{userId}")
    public HttpResponse<UserDto> getUserById(@PathVariable Long userId) {
        final UserDto user = userService.getUserById(userId);
        return HttpResponse.ok(user);
    }
}
