package com.expenshare.service.expense;

import com.expenshare.model.dto.expense.CreateExpenseRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface for validating expense input and calculating user balances
 * based on different split types.
 */
public interface SplitValidator {
    /**
     * Validates the expense input according to the split type.
     *
     * @param req the expense creation request
     * @throws com.expenshare.exception.ValidationException if input is invalid
     */
    void validateExpenseInputOrThrow(CreateExpenseRequest req);

    /**
     * Calculates the user balances from the provided shares.
     *
     * @param payerId the user ID of the payer
     * @param amount the total expense amount
     * @param shares the list of shares for each participant
     * @return list of user balances
     */
    List<UserBalance> calculateUserBalancesFromShares(
            Long payerId,
            BigDecimal amount,
            List<CreateExpenseRequest.Share> shares
    );
}
