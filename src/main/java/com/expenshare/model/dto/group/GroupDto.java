package com.expenshare.model.dto.group;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Serdeable
@Getter
@Setter
public class GroupDto {
    @NotNull(message = "Group ID cannot be null")
    @NotBlank(message = "Group ID cannot be blank")
    private Long groupId;

    @NotNull(message = "Group name cannot be null")
    @NotBlank(message = "Group name cannot be blank")
    private String name;

    @NotNull(message = "Members list cannot be null")
    @NotEmpty(message = "Members list cannot be empty")
    private List<Long> members;

    @NotNull(message = "Creation timestamp cannot be null")
    private LocalDateTime createdAt;
}
