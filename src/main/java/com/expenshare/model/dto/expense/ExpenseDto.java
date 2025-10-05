package com.expenshare.model.dto.expense;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Serdeable
@Getter
@Setter
public class ExpenseDto {
    @NotNull
    private Long expenseId;

    @NotNull
    private Long groupId;

    @NotNull
    private Long paidBy;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @NotNull
    @Size(min = 1)
    private List<@Valid Split> split;

    @NotNull
    private LocalDateTime createdAt;

    @Serdeable
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Split {
        @NotNull
        private Long userId;

        @NotNull
        private BigDecimal share;
    }
}
