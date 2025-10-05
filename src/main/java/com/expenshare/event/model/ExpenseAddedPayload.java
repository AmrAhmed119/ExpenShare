package com.expenshare.event.model;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record ExpenseAddedPayload(Long expenseId, Long groupId, Long paidBy, BigDecimal amount, String description) {
}
