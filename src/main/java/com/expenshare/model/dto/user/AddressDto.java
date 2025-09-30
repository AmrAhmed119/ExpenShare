package com.expenshare.model.dto.user;

import com.expenshare.validation.country.CountryCode;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Serdeable
@Getter
@Setter
public class AddressDto {
    @NotNull(message = "Address line1 cannot be null")
    @NotBlank(message = "Address line1 cannot be blank")
    private String line1;

    private String line2;

    private String city;

    private String state;

    private String postalCode;

    @CountryCode
    private String country;
}