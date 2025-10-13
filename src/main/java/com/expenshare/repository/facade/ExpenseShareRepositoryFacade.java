package com.expenshare.repository.facade;

import com.expenshare.service.expense.UserBalance;
import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.ExpenseShareEntity;
import com.expenshare.model.entity.UserEntity;
import com.expenshare.repository.ExpenseShareRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.util.List;

@Transactional
@Singleton
public class ExpenseShareRepositoryFacade {
    private final ExpenseShareRepository expenseShareRepository;

    private final ExpenseRepositoryFacade expenseRepositoryFacade;

    private final UserRepositoryFacade userRepositoryFacade;

    public ExpenseShareRepositoryFacade(
        ExpenseShareRepository expenseShareRepository,
        ExpenseRepositoryFacade expenseRepositoryFacade,
        UserRepositoryFacade userRepositoryFacade
    ) {
        this.expenseShareRepository = expenseShareRepository;
        this.expenseRepositoryFacade = expenseRepositoryFacade;
        this.userRepositoryFacade = userRepositoryFacade;
    }

    public List<ExpenseShareEntity> saveAllShares(Long expenseId, List<UserBalance> shares) {
        return shares
                .stream()
                .map(share -> {
                    final ExpenseEntity expense = expenseRepositoryFacade.getOrThrow(expenseId);
                    final UserEntity user = userRepositoryFacade.getOrThrow(share.getUserId());
                    return expenseShareRepository.save(
                    ExpenseShareEntity.builder()
                        .expense(expense)
                        .user(user)
                        .shareAmount(share.getBalance())
                        .build()
                    );
                }).toList();
    }

    public void update(ExpenseShareEntity expenseShare) {
        expenseShareRepository.update(expenseShare);
    }
}
