package com.expenshare.event.model;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record SettlementConfirmedPayload(
    Long settlementId,
    Long expenseId,
    Long fromUserId,
    Long toUserId,
    BigDecimal amount
) {
}
