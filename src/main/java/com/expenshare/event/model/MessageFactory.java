package com.expenshare.event.model;

import com.expenshare.model.entity.ExpenseEntity;
import com.expenshare.model.entity.SettlementEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageFactory {
    public static EventMessage<EntityCreatedPayload> entityCreatedMessage(Long entityId) {
        return new EventMessage<>(
                UUID.randomUUID(),
                LocalDateTime.now(),
                new EntityCreatedPayload(entityId)
        );
    }

    public static EventMessage<NotificationWelcomePayload> welcomeNotificationMessage(
        String targetType,
        Long targetId,
        String channel
    ) {
        return new EventMessage<>(
                UUID.randomUUID(),
                LocalDateTime.now(),
                new NotificationWelcomePayload(targetType, targetId, channel)
        );
    }

    public static EventMessage<ExpenseAddedPayload> expenseAddedMessage(ExpenseEntity entity) {
        return new EventMessage<>(
                UUID.randomUUID(),
                LocalDateTime.now(),
                new ExpenseAddedPayload(
                    entity.getId(),
                    entity.getGroup().getId(),
                    entity.getPaidBy().getId(),
                    entity.getAmount(),
                    entity.getDescription()
                )
        );
    }

    public static EventMessage<SettlementConfirmedPayload> settlementConfirmedMessage(SettlementEntity settlement) {
        return new EventMessage<>(
                UUID.randomUUID(),
                LocalDateTime.now(),
                new SettlementConfirmedPayload(
                    settlement.getId(),
                    settlement.getExpense().getId(),
                    settlement.getFromUser().getId(),
                    settlement.getToUser().getId(),
                    settlement.getAmount()
                )
        );
    }
}
