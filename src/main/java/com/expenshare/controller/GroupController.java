package com.expenshare.controller;

import com.expenshare.model.dto.group.AddMembersDto;
import com.expenshare.model.dto.group.AddMembersRequest;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.service.GroupService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Controller("/api/groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(
        summary = "Create Group",
        description = "Creates a new group with the provided name and members."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Group created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GroupDto.class),
                examples = @ExampleObject(
                    value = "{ \"groupId\": 10, \"name\": \"Trip to Dubai\", \"members\": [1,2,3], \"createdAt\": \"2025-09-27T15:00:00Z\" }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "One or more users not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"errorCode\": \"NOT_FOUND\", \"message\": \"One or more users not found\" }"
                )
            )
        )
    })
    @Post
    public HttpResponse<GroupDto> createGroup(@Body CreateGroupRequest createGroupRequest) {
        final GroupDto group = groupService.createGroup(createGroupRequest);
        return HttpResponse.created(group);
    }

    @Operation(
        summary = "Get Group",
        description = "Retrieves a group by its unique ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Group found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GroupDto.class),
                examples = @ExampleObject(
                    value = "{ \"groupId\": 10, \"name\": \"Trip to Dubai\", \"members\": [1,2,3] }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Group not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"errorCode\": \"NOT_FOUND\", \"message\": \"Group not found\" }"
                )
            )
        )
    })
    @Get("/{groupId}")
    public HttpResponse<GroupDto> getGroupById(@PathVariable Long groupId) {
        final GroupDto group = groupService.getGroupById(groupId);
        return HttpResponse.ok(group);
    }

    @Operation(
        summary = "Add Members to Group",
        description = "Adds members to an existing group."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Members added successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AddMembersDto.class),
                examples = @ExampleObject(
                    value = "{ \"groupId\": 10, \"membersAdded\": [4,5], \"totalMembers\": 5 }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Group not found | One or more users not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"errorCode\": \"NOT_FOUND\", \"message\": \"Group not found | One or more users not found\" }"
                )
            )
        ),
    })
    @Post("/{groupId}/members")
    public HttpResponse<AddMembersDto> addMembersToGroup(
        @PathVariable Long groupId,
        @Body AddMembersRequest addMembersRequest
    ) {
        final AddMembersDto addMembersDto = groupService.addMembersToGroup(groupId, addMembersRequest);
        return HttpResponse.ok(addMembersDto);
    }
}
