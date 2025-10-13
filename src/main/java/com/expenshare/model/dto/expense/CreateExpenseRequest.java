package com.expenshare.model.dto.expense;

import com.expenshare.model.enums.SplitType;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Serdeable
@Getter
@Setter
public class CreateExpenseRequest {
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
    private SplitType splitType;

    // Optional for EQUAL, required for EXACT/PERCENT (validate in service)
    private List<@NotNull Long> participants;

    // Required for EXACT/PERCENT (validate in service)
    private List<@Valid Share> shares;

    @Serdeable
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Share {
        @NotNull
        private Long userId;

        private BigDecimal amount; // for EXACT

        private Double percent;   // for PERCENT
    }
}
