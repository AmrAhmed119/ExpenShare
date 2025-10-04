package com.expenshare.event.model;

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
}
