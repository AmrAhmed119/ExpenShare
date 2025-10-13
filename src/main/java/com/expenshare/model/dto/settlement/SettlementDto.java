package com.expenshare.model.dto.settlement;

import com.expenshare.model.enums.Method;
import com.expenshare.model.enums.Status;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Serdeable
@Getter
@Setter
public class SettlementDto {
    @NotNull
    private Long settlementId;

    @NotNull
    private Long expenseId;

    @NotNull
    private Long fromUserId;

    @NotNull
    private Long toUserId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private Method method;

    @Size(max = 255)
    private String note;

    @NotNull
    private Status status;

    @NotNull
    private LocalDateTime createdAt;
}
