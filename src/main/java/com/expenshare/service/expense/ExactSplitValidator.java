package com.expenshare.service.expense;

import com.expenshare.exception.ValidationException;
import com.expenshare.model.dto.expense.CreateExpenseRequest;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.List;

@Singleton
public class ExactSplitValidator implements SplitValidator {
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

        BigDecimal totalShare = BigDecimal.ZERO;
        for (CreateExpenseRequest.Share share : shares) {
            if (!participants.contains(share.getUserId())) {
                throw new ValidationException("Share user Ids must be in participants list.");
            }
            if (share.getAmount() == null || share.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Each share amount must be > 0.");
            }
            totalShare = totalShare.add(share.getAmount());
        }

        if (totalShare.compareTo(req.getAmount()) != 0) {
            throw new ValidationException("Split amounts must total expense amount.");
        }
    }

    @Override
    public List<UserBalance> calculateUserBalancesFromShares(Long payerId, BigDecimal amount, List<CreateExpenseRequest.Share> shares) {
        return shares
                .stream()
                .map(share -> {
                    BigDecimal shareAmount = share.getAmount();
                    if (share.getUserId().equals(payerId)) {
                        shareAmount = shareAmount.subtract(amount);
                    }
                    return new UserBalance(share.getUserId(), shareAmount);
                })
                .toList();
    }
}
