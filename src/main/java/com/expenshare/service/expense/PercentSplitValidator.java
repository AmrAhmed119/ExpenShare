package com.expenshare.service.expense;

import com.expenshare.exception.ValidationException;
import com.expenshare.model.dto.expense.CreateExpenseRequest;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Singleton
public class PercentSplitValidator implements SplitValidator {
    @Override
    public void validateExpenseInputOrThrow(CreateExpenseRequest req) {
        List<Long> participants = req.getParticipants();
        List<CreateExpenseRequest.Share> shares = req.getShares();

        if (participants == null || participants.size() < 2) {
            throw new ValidationException("At least two participants are required for a split.");
        }

        if (shares == null || shares.size() != participants.size()) {
            throw new ValidationException("Shares must be provided for all participants.");
        }

        Double percent = 0.0;
        for (CreateExpenseRequest.Share share : shares) {
            if (!participants.contains(share.getUserId())) {
                throw new ValidationException("Share user Ids must be in participants list.");
            }
            if (share.getPercent() == null || share.getPercent() <= 0.0 || share.getPercent() > 100.0) {
                throw new ValidationException("Each share percent must be > 0 and <= 100.");
            }
            percent += share.getPercent();
        }

        if (Double.compare(percent, 100.0) != 0) {
            throw new ValidationException("Split percentages must total 100.");
        }
    }

    @Override
    public List<UserBalance> calculateUserBalancesFromShares(Long payerId, BigDecimal amount, List<CreateExpenseRequest.Share> shares) {
        return shares
                .stream()
                .map(share -> {
                    BigDecimal shareAmount = amount
                            .multiply(BigDecimal.valueOf(share.getPercent()))
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                    if (share.getUserId().equals(payerId)) {
                        shareAmount = shareAmount.subtract(amount);
                    }
                    return new UserBalance(share.getUserId(), shareAmount);
                })
                .toList();
    }
}
