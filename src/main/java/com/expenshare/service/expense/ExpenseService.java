package com.expenshare.service.expense;

import com.expenshare.event.KafkaProducer;
import com.expenshare.event.model.MessageFactory;
import com.expenshare.exception.NotFoundException;
import com.expenshare.exception.ValidationException;
import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.model.dto.expense.ShareDto;
import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.ExpenseShareEntity;
import com.expenshare.model.mapper.ExpenseMapper;
import com.expenshare.model.mapper.ExpenseShareMapper;
import com.expenshare.repository.facade.*;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Service for expense-related operations such as creation, validation, and balance calculation.
 * Handles persistence and event publishing for expenses.
 */
@Singleton
public class ExpenseService {
    private final ExpenseRepositoryFacade expenseRepositoryFacade;
    private final GroupRepositoryFacade groupRepositoryFacade;
    private final GroupMemberRepositoryFacade groupMemberRepositoryFacade;
    private final ExpenseMapper expenseMapper;
    private final UserRepositoryFacade userRepositoryFacade;
    private final ExpenseShareRepositoryFacade expenseShareRepositoryFacade;
    private final KafkaProducer kafkaProducer;
    private final ExpenseShareMapper expenseShareMapper;
    private final SplitValidatorFactory splitValidatorFactory;

    public ExpenseService(
        ExpenseRepositoryFacade expenseRepositoryFacade,
        GroupRepositoryFacade groupRepositoryFacade,
        GroupMemberRepositoryFacade groupMemberRepositoryFacade,
        ExpenseMapper expenseMapper,
        UserRepositoryFacade userRepositoryFacade,
        ExpenseShareRepositoryFacade expenseShareRepositoryFacade,
        KafkaProducer kafkaProducer,
        ExpenseShareMapper expenseShareMapper,
        SplitValidatorFactory splitValidatorFactory
    ) {
        this.expenseRepositoryFacade = expenseRepositoryFacade;
        this.groupRepositoryFacade = groupRepositoryFacade;
        this.groupMemberRepositoryFacade = groupMemberRepositoryFacade;
        this.expenseMapper = expenseMapper;
        this.userRepositoryFacade = userRepositoryFacade;
        this.expenseShareRepositoryFacade = expenseShareRepositoryFacade;
        this.kafkaProducer = kafkaProducer;
        this.expenseShareMapper = expenseShareMapper;
        this.splitValidatorFactory = splitValidatorFactory;
    }

    private void isGroupExistOrThrow(Long groupId) {
        if (!groupRepositoryFacade.existsById(groupId)) {
            throw new NotFoundException("Group not found");
        }
    }

    private void isPayerAndParticipantsInGroupOrThrow(Long groupId, Long payerId, List<Long> participantIds) {
        final List<Long> allParticipants = new ArrayList<>(List.of(payerId));
        allParticipants.addAll(participantIds != null ? new ArrayList<>(participantIds) : List.of());

        final boolean isAllExist = allParticipants.stream().allMatch(
            participantId -> groupMemberRepositoryFacade.existsByGroupIdAndUserId(groupId, participantId)
        );

        if (!isAllExist) {
            throw new ValidationException("Participants must be group members");
        }
    }

    /**
     * Adds a new expense to a group, validates input, persists data, and publishes related events.
     *
     * @param createExpenseRequest the request containing expense data
     * @return the created expense as a DTO
     * @throws NotFoundException if the group or participants are not found
     * @throws com.expenshare.exception.ValidationException if the expense input is invalid
     */
    public ExpenseDto addExpense(CreateExpenseRequest createExpenseRequest) {
        final Long groupId = createExpenseRequest.getGroupId();

        // checks: check group existence
        isGroupExistOrThrow(groupId);

        // checks: check if payer and participants are actual group members
        isPayerAndParticipantsInGroupOrThrow(
            groupId,
            createExpenseRequest.getPaidBy(),
            createExpenseRequest.getParticipants()
        );

        // checks: check the shares and participants
        final SplitValidator splitValidator = splitValidatorFactory.getValidator(createExpenseRequest.getSplitType());
        splitValidator.validateExpenseInputOrThrow(createExpenseRequest);
        final List<UserBalance> userBalances = splitValidator.calculateUserBalancesFromShares(
                createExpenseRequest.getPaidBy(),
                createExpenseRequest.getAmount(),
                createExpenseRequest.getShares()
        );

        // saves: save the expense entity
        final ExpenseEntity savedEntity = expenseMapper.toEntity(createExpenseRequest);
        savedEntity.setGroup(groupRepositoryFacade.getOrThrow(groupId));
        savedEntity.setPaidBy(userRepositoryFacade.getOrThrow(createExpenseRequest.getPaidBy()));
        expenseRepositoryFacade.saveExpense(savedEntity);

        // saves: save the shares
        expenseShareRepositoryFacade.saveAllShares(savedEntity.getId(), userBalances);

        // publishes: send to kafka
        kafkaProducer.publishExpenseAddedEvent(
            MessageFactory.expenseAddedMessage(savedEntity)
        );

        return expenseMapper.toDto(savedEntity, userBalances);
    }

    // check if the other instant is before the given utcTime
    private boolean isTimeBefore(ZonedDateTime utcTime, LocalDateTime otherTime) {
        Instant utcInstant = utcTime.toInstant();
        Instant otherInstant = otherTime.atZone(ZoneId.systemDefault()).toInstant();
        return otherInstant.isBefore(utcInstant);
    }

    private List<UserBalance> getUsersBalanceBeforeTime(List<ExpenseEntity> expenses, ZonedDateTime utcTime) {
        final Map<Long, BigDecimal> userBalanceMap = new HashMap<>();

        for (ExpenseEntity expense : expenses) {
            if (isTimeBefore(utcTime, expense.getCreatedAt())) {
                final Set<ExpenseShareEntity> shares = expense.getShares();
                for (ExpenseShareEntity share : shares) {
                    final Long userId = share.getUser().getId();
                    final BigDecimal currentBalance = userBalanceMap.getOrDefault(userId, BigDecimal.ZERO);
                    userBalanceMap.put(userId, currentBalance.add(share.getShareAmount()));
                }
            }
        }

        return userBalanceMap.entrySet()
                .stream()
                .map(entry -> new UserBalance(entry.getKey(), entry.getValue()))
                .toList();
    }

    /**
     * Retrieves the balances of all users in a group as of a snapshot time.
     *
     * @param groupId the group ID
     * @param snapshotTime the snapshot time (if null, uses current time)
     * @return DTO containing group balances
     * @throws NotFoundException if the group is not found
     */
    public ShareDto getGroupBalances(Long groupId, LocalDateTime snapshotTime) {
        LocalDateTime time = (snapshotTime != null) ? snapshotTime : LocalDateTime.now();
        final ZonedDateTime utcTime = time.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"));

        final List<ExpenseEntity> expenses = expenseRepositoryFacade.findByGroupId(groupId);
        final List<UserBalance> balances = getUsersBalanceBeforeTime(expenses, utcTime);

        return expenseShareMapper.toDto(groupId, balances, LocalDateTime.now());
    }
}
