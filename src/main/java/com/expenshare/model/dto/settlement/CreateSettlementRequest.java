package com.expenshare.model.dto.settlement;

import com.expenshare.model.enums.Method;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Serdeable
@Getter
@Setter
public class CreateSettlementRequest {
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
    private Method method = Method.OTHER;

    @Size(max = 255)
    private String note;

    @Size(max = 64)
    private String reference;

    private boolean enforceOwedLimit = true;
}
