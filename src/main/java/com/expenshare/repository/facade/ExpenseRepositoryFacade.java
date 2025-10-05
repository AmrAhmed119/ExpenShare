package com.expenshare.repository.facade;

import com.expenshare.exception.NotFoundException;
import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.repository.ExpenseRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.util.List;

@Transactional
@Singleton
public class ExpenseRepositoryFacade {
    private final ExpenseRepository expenseRepository;

    public ExpenseRepositoryFacade(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ExpenseEntity getOrThrow(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new NotFoundException("Expense not found"));
    }

    public ExpenseEntity saveExpense(ExpenseEntity expenseEntity) {
        return expenseRepository.save(expenseEntity);
    }

    public List<ExpenseEntity> findByGroupId(Long groupId) {
        return expenseRepository.findByGroupId(groupId);
    }
}
