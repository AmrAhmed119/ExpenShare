package com.expenshare.model.dto.settlement;

import com.expenshare.model.enums.Status;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Serdeable
@Getter
@Setter
public class SettlementStatusDto {
    @NotNull
    private Long settlementId;

    @NotNull
    private Status status;

    private LocalDateTime confirmedAt;
}
