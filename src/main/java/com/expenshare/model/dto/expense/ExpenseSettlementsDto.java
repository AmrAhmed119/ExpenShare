package com.expenshare.model.dto.expense;

import com.expenshare.model.enums.Status;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Serdeable
@Getter
@Setter
@AllArgsConstructor
public class ExpenseSettlementsDto {
    @NotNull
    private Long expenseId;

    @NotNull
    private List<@Valid Item> items;

    @NotNull
    @Min(0)
    private int page;

    @NotNull
    @Min(0)
    @Max(100)
    private int size;

    @NotNull
    private int total;

    @Serdeable
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Item {
        @NotNull
        private Long settlementId;

        @NotNull
        private Long fromUserId;

        @NotNull
        private Long toUserId;

        @NotNull
        @DecimalMin(value = "0.01")
        private BigDecimal amount;

        @NotNull
        private Status status;
    }
}
