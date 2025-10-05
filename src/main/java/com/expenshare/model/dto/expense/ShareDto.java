package com.expenshare.model.dto.expense;

import com.expenshare.service.expense.UserBalance;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Serdeable
@Getter
@Setter
@AllArgsConstructor
public class ShareDto {
    @NotNull
    private Long groupId;

    @NotNull
    @Size(min = 1)
    private List<@Valid UserBalance> balances;

    @NotNull
    private LocalDateTime calculatedAt;
}
