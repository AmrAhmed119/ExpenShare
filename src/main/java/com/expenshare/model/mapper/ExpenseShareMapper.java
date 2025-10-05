package com.expenshare.model.mapper;

import com.expenshare.model.dto.expense.ShareDto;
import com.expenshare.service.expense.UserBalance;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta")
public interface ExpenseShareMapper {
    ShareDto toDto(Long groupId, List<UserBalance> balances, LocalDateTime calculatedAt);
}
