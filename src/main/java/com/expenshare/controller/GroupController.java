package com.expenshare.controller;

import com.expenshare.model.dto.group.AddMembersDto;
import com.expenshare.model.dto.group.AddMembersRequest;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

@Controller("/api/groups")
public class GroupController {

    @Post
    public HttpResponse<GroupDto> createGroup(@Body CreateGroupRequest createGroupRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Get("/{groupId}")
    public HttpResponse<GroupDto> getGroupById(@PathVariable Long groupId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Post("/{groupId}/members")
    public HttpResponse<AddMembersDto> addMembersToGroup(
        @PathVariable Long groupId,
        @Body AddMembersRequest addMembersRequest
    ) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
