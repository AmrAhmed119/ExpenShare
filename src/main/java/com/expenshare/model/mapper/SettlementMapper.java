package com.expenshare.model.mapper;

import com.expenshare.model.dto.settlement.CreateSettlementRequest;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.model.dto.settlement.SettlementStatusDto;
import com.expenshare.model.entity.SettlementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface SettlementMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expense", ignore = true)
    @Mapping(target = "fromUser", ignore = true)
    @Mapping(target = "toUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "confirmedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    SettlementEntity toEntity(CreateSettlementRequest req);

    @Mapping(target = "settlementId", source = "id")
    @Mapping(target = "expenseId", source = "expense.id")
    @Mapping(target = "fromUserId", source = "fromUser.id")
    @Mapping(target = "toUserId", source = "toUser.id")
    @Mapping(target = "createdAt", source = "createdAt")
    SettlementDto toDto(SettlementEntity entity);

    @Mapping(target = "settlementId", source = "id")
    SettlementStatusDto toStatusDto(SettlementEntity entity);
}
