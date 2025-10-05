package com.expenshare.service.expense;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Serdeable
@Getter
@Setter
@AllArgsConstructor
public class UserBalance {
    @NotNull
    private Long userId;

    @NotNull
    private BigDecimal balance;
}
