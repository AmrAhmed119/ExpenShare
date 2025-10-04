package com.expenshare.model.dto.group;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Serdeable
@Getter
@Setter
@AllArgsConstructor
public class AddMembersDto {
    @NotNull(message = "Group ID cannot be null")
    @NotBlank(message = "Group ID cannot be blank")
    private Long groupId;

    @NotNull(message = "Members list cannot be null")
    @NotEmpty(message = "Members list cannot be empty")
    private List<Long> membersAdded;

    @NotNull(message = "Total members cannot be null")
    @Positive(message = "Total members must be positive")
    private int totalMembers;
}
