package com.expenshare.event;

import com.expenshare.event.model.*;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface KafkaProducer {
    @Topic("user.created")
    void publishUserCreatedEvent(EventMessage<EntityCreatedPayload> message);

    @Topic("notification.welcome")
    void publishWelcomeNotificationEvent(EventMessage<NotificationWelcomePayload> message);

    @Topic("group.created")
    void publishGroupCreatedEvent(EventMessage<EntityCreatedPayload> message);

    @Topic("expense.added")
    void publishExpenseAddedEvent(EventMessage<ExpenseAddedPayload> message);

    @Topic("settlement.confirmed")
    void publishSettlementConfirmedEvent(EventMessage<SettlementConfirmedPayload> message);
}
