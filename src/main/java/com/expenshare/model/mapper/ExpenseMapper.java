package com.expenshare.model.mapper;

import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.service.expense.UserBalance;
import com.expenshare.model.entity.ExpenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface ExpenseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)  // handled in service
    @Mapping(target = "paidBy", ignore = true)  // handled in service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "shares", ignore = true)
    @Mapping(target = "settlements", ignore = true)
    ExpenseEntity toEntity(CreateExpenseRequest req);

    @Mapping(target = "expenseId", source = "e.id")
    @Mapping(target = "groupId", source = "e.group.id")
    @Mapping(target = "paidBy", source = "e.paidBy.id")
    @Mapping(target = "amount", source = "e.amount")
    @Mapping(target = "description", source = "e.description")
    @Mapping(target = "createdAt", source = "e.createdAt")
    @Mapping(target = "split", source = "balances", qualifiedByName = "mapSharesToSplits")
    ExpenseDto toDto(ExpenseEntity e, List<UserBalance> balances);

    @Named("mapSharesToSplits")
    default List<ExpenseDto.Split> mapSharesToSplits(List<UserBalance> balances) {
        if (balances == null) return List.of();
        return balances
                .stream()
                .map(balance ->
                     new ExpenseDto.Split(balance.getUserId(), balance.getBalance())
                )
                .toList();
    }
}
