package com.expenshare.service.Settlement;

import com.expenshare.event.KafkaProducer;
import com.expenshare.event.model.MessageFactory;
import com.expenshare.exception.ConflictException;
import com.expenshare.exception.ValidationException;
import com.expenshare.model.dto.expense.ExpenseSettlementsDto;
import com.expenshare.model.dto.settlement.CreateSettlementRequest;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.model.dto.settlement.SettlementStatusDto;
import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.ExpenseShareEntity;
import com.expenshare.model.entity.SettlementEntity;
import com.expenshare.model.enums.Status;
import com.expenshare.model.mapper.SettlementMapper;
import com.expenshare.repository.facade.ExpenseRepositoryFacade;
import com.expenshare.repository.facade.ExpenseShareRepositoryFacade;
import com.expenshare.repository.facade.SettlementRepositoryFacade;
import com.expenshare.repository.facade.UserRepositoryFacade;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Singleton
public class SettlementService {
    private final SettlementRepositoryFacade settlementRepositoryFacade;

    private final ExpenseRepositoryFacade expenseRepositoryFacade;

    private final SettlementMapper settlementMapper;

    private final UserRepositoryFacade userRepositoryFacade;

    private final ExpenseShareRepositoryFacade expenseShareRepositoryFacade;

    private final KafkaProducer kafkaProducer;

    public SettlementService(
        SettlementRepositoryFacade settlementRepositoryFacade,
        ExpenseRepositoryFacade expenseRepositoryFacade,
        SettlementMapper settlementMapper,
        UserRepositoryFacade userRepositoryFacade,
        ExpenseShareRepositoryFacade expenseShareRepositoryFacade,
        KafkaProducer kafkaProducer
    ) {
        this.settlementRepositoryFacade = settlementRepositoryFacade;
        this.expenseRepositoryFacade = expenseRepositoryFacade;
        this.settlementMapper = settlementMapper;
        this.userRepositoryFacade = userRepositoryFacade;
        this.expenseShareRepositoryFacade = expenseShareRepositoryFacade;
        this.kafkaProducer = kafkaProducer;
    }

    private void validateExpenseDataOrThrow(Long expenseId, Long fromUserId, Long toUserId) {
        final ExpenseEntity expense = expenseRepositoryFacade.getOrThrow(expenseId);

        if (!Objects.equals(expense.getPaidBy().getId(), toUserId)) {
            throw new ValidationException("toUserId must be the paidBy user of the expense");
        }

        if (Objects.equals(fromUserId, toUserId)) {
            throw new ValidationException("fromUserId and toUserId cannot be the same");
        }

        if (expense.getGroup().getMembers()
                .stream()
                .noneMatch(member -> Objects.equals(member.getUser().getId(), fromUserId))) {
            throw new ValidationException("fromUserId must be a member of the expense's group");
        }
    }

    private ExpenseShareEntity getUserShareEntity(Long expenseId, Long UserId) {
        return expenseRepositoryFacade.getOrThrow(expenseId)
                .getShares()
                .stream()
                .filter(share -> Objects.equals(share.getUser().getId(), UserId))
                .findFirst()
                .orElseThrow(() -> new ValidationException("User has no share in the expense"));
    }

    private BigDecimal getOwedAmount(Long expenseId, Long fromUserId) {
        return getUserShareEntity(expenseId, fromUserId).getShareAmount();
    }

    public SettlementDto createSettlement(CreateSettlementRequest createSettlementRequest) {
        final Long expenseId = createSettlementRequest.getExpenseId();
        final Long fromUserId = createSettlementRequest.getFromUserId();
        final Long toUserId = createSettlementRequest.getToUserId();
        final BigDecimal amount = createSettlementRequest.getAmount();

        validateExpenseDataOrThrow(expenseId, fromUserId, toUserId);

        final BigDecimal owedAmount = getOwedAmount(expenseId, fromUserId);

        if (createSettlementRequest.isEnforceOwedLimit()) {
            if (amount.compareTo(owedAmount) > 0) {
                throw new ValidationException("Cannot settle more than owed");
            }
        }

        final SettlementEntity settlement = settlementMapper.toEntity(createSettlementRequest);
        settlement.setExpense(expenseRepositoryFacade.getOrThrow(expenseId));
        settlement.setFromUser(userRepositoryFacade.getOrThrow(fromUserId));
        settlement.setToUser(userRepositoryFacade.getOrThrow(toUserId));

        final SettlementEntity savedSettlement = settlementRepositoryFacade.save(settlement);

        return settlementMapper.toDto(savedSettlement);
    }

    public SettlementStatusDto confirmSettlement(Long settlementId) {
        final SettlementEntity settlement = settlementRepositoryFacade.getOrThrow(settlementId);

        if (settlement.getStatus() == Status.CONFIRMED) {
            throw new ConflictException("Settlement is already confirmed");
        }

        settlement.setStatus(Status.CONFIRMED);
        settlement.setConfirmedAt(LocalDateTime.now());
        final SettlementEntity updatedSettlement =  settlementRepositoryFacade.update(settlement);

        // update the owed amount in ExpenseShareEntity for both users
        final Long fromUserId = updatedSettlement.getFromUser().getId();
        final Long toUserId = updatedSettlement.getToUser().getId();
        final Long expenseId = updatedSettlement.getExpense().getId();

        final ExpenseShareEntity fromUserShare = getUserShareEntity(expenseId, fromUserId);
        final ExpenseShareEntity toUserShare = getUserShareEntity(expenseId, toUserId);

        final BigDecimal toBeRemoved = updatedSettlement.getAmount().min(getOwedAmount(expenseId, fromUserId));

        fromUserShare.setShareAmount(fromUserShare.getShareAmount().subtract(toBeRemoved));
        toUserShare.setShareAmount(toUserShare.getShareAmount().add(toBeRemoved));

        expenseShareRepositoryFacade.update(fromUserShare);
        expenseShareRepositoryFacade.update(toUserShare);

        kafkaProducer.publishSettlementConfirmedEvent(MessageFactory.settlementConfirmedMessage(updatedSettlement));

        return settlementMapper.toStatusDto(updatedSettlement);
    }

    public SettlementStatusDto cancelSettlement(Long settlementId) {
        final SettlementEntity settlement = settlementRepositoryFacade.getOrThrow(settlementId);

        if (settlement.getStatus() != Status.PENDING) {
            throw new ConflictException("Only pending settlements can be canceled");
        }

        settlement.setStatus(Status.CANCELED);
        final SettlementEntity updatedSettlement =  settlementRepositoryFacade.update(settlement);

        return settlementMapper.toStatusDto(updatedSettlement);
    }

    public ExpenseSettlementsDto filterExpenseSettlements(
        Long expenseId,
        Status status,
        Long fromUserId,
        Long toUserId,
        int page,
        int size
    ) {
        PredicateSpecification<SettlementEntity> specs = SettlementSpecifications.hasExpenseId(expenseId);

        if (status != null) specs = specs.and(SettlementSpecifications.hasStatus(status));
        if (fromUserId != null) specs = specs.and(SettlementSpecifications.hasFromUserId(fromUserId));
        if (toUserId != null) specs = specs.and(SettlementSpecifications.hasToUserId(toUserId));

        Pageable pageable = Pageable.from(page, size);

        final Page<SettlementEntity> settlementPage = settlementRepositoryFacade
                .filterExpenseSettlements(specs, pageable);

        final List<ExpenseSettlementsDto.Item> items = settlementPage
                .getContent()
                .stream()
                .map((settlement) ->  new ExpenseSettlementsDto.Item(
                        settlement.getId(),
                        settlement.getFromUser().getId(),
                        settlement.getToUser().getId(),
                        settlement.getAmount(),
                        settlement.getStatus()
                )).toList();

        return new ExpenseSettlementsDto(
                expenseId,
                items,
                settlementPage.getPageNumber(),
                settlementPage.getSize(),
                (int) settlementPage.getTotalSize()
        );
    }
}
