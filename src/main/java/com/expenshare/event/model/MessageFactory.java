package com.expenshare.event.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageFactory {
    public static EventMessage<UserCreatedPayload> userCreatedMessage(Long userId) {
        return new EventMessage<>(
                UUID.randomUUID(),
                LocalDateTime.now(),
                new UserCreatedPayload(userId)
        );
    }

    public static EventMessage<NotificationWelcomePayload> welcomeNotificationMessage(String targetType) {
        return new EventMessage<>(
                UUID.randomUUID(),
                LocalDateTime.now(),
                new NotificationWelcomePayload(targetType)
        );
    }
}
