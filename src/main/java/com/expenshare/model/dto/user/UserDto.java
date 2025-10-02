package com.expenshare.model.dto.user;

import com.expenshare.validation.mobile.E164;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Serdeable
@Getter
@Setter
public class UserDto {
    @NotNull(message = "User ID cannot be null")
    @NotBlank(message = "User ID cannot be blank")
    private Long id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @E164
    private String mobileNumber;

    private @Valid AddressDto address;

    @NotNull(message = "Creation timestamp cannot be null")
    @NotBlank(message = "Creation timestamp cannot be blank")
    private LocalDateTime createdAt;
}
