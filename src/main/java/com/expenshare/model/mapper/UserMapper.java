package com.expenshare.model.mapper;

import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface UserMapper {
    @Mapping(target = "addressLine1", source = "address.line1")
    @Mapping(target = "addressLine2", source = "address.line2")
    @Mapping(target = "addressCity", source = "address.city")
    @Mapping(target = "addressState", source = "address.state")
    @Mapping(target = "addressPostal", source = "address.postalCode")
    @Mapping(target = "addressCountry", source = "address.country")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "groupMemberships", ignore = true)
    UserEntity toEntity(CreateUserRequest req);

    @Mapping(target = "address.line1", source = "addressLine1")
    @Mapping(target = "address.line2", source = "addressLine2")
    @Mapping(target = "address.city", source = "addressCity")
    @Mapping(target = "address.state", source = "addressState")
    @Mapping(target = "address.postalCode", source = "addressPostal")
    @Mapping(target = "address.country", source = "addressCountry")
    UserDto toDto(UserEntity entity);
}
