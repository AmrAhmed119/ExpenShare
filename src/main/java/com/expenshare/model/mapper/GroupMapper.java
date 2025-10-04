package com.expenshare.model.mapper;

import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.entity.GroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface GroupMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "members", ignore = true)
    GroupEntity toEntity(CreateGroupRequest req);

    @Mapping(target = "groupId", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "createdAt", source = "entity.createdAt")
    @Mapping(target = "members", source = "memberIds")
    GroupDto toDto(GroupEntity entity, List<Long> memberIds);
}
