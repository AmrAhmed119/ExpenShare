package com.expenshare.event;

import com.expenshare.event.model.EventMessage;
import com.expenshare.event.model.NotificationWelcomePayload;
import com.expenshare.event.model.UserCreatedPayload;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface KafkaProducer {
    @Topic("user.created")
    void publishUserCreatedEvent(EventMessage<UserCreatedPayload> message);

    @Topic("notification.welcome")
    void publishWelcomeNotificationEvent(EventMessage<NotificationWelcomePayload> message);
}
