package com.expenshare.service.expense;

import com.expenshare.exception.ValidationException;
import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.repository.facade.GroupMemberRepositoryFacade;
import io.micronaut.context.annotation.Prototype;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Prototype
public class EqualSplitValidator implements SplitValidator {
    List<Long> participants;

    @Inject
    GroupMemberRepositoryFacade groupMemberRepositoryFacade;

    private List<Long>  getAllGroupMemberIds(Long groupId) {
        return groupMemberRepositoryFacade.findAllByGroupId(groupId)
                .stream()
                .map(groupMemberEntity -> groupMemberEntity.getUser().getId())
                .toList();
    }

    @Override
    public void validateExpenseInputOrThrow(CreateExpenseRequest req) {
        participants = req.getParticipants();
        if (participants == null) {
            participants = getAllGroupMemberIds(req.getGroupId());
        }

        if (participants.size() < 2) {
            throw new ValidationException("At least two participants are required for a split.");
        }
    }

    @Override
    public List<UserBalance> calculateUserBalancesFromShares(
            Long payerId,
            BigDecimal amount,
            List<CreateExpenseRequest.Share> shares
    ) {
        // skip shares since amount is to be split equally anyway.
        final BigDecimal equalShare = amount.divide(BigDecimal.valueOf(participants.size()), RoundingMode.HALF_UP);

        return participants
                .stream()
                .map(participantId -> {
                    BigDecimal shareAmount = equalShare;
                    if (participantId.equals(payerId)) {
                        shareAmount = shareAmount.subtract(amount);
                    }
                    return new UserBalance(participantId, shareAmount);
                })
                .toList();
    }
}
