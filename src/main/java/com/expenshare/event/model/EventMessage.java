package com.expenshare.event.model;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;
import java.util.UUID;

@Serdeable
public record EventMessage<T>(UUID eventId, LocalDateTime occurredAt, T payload) {
}
