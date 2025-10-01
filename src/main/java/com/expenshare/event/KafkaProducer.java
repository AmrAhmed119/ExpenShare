package com.expenshare.event;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

// TODO: Should be updated to include message payload instead of just using strings
@KafkaClient
public interface KafkaProducer {
    @Topic("user.created")
    void publishUserCreatedEvent(String message);

    @Topic("notification.welcome")
    void publishWelcomeNotificationEvent(String message);
}
