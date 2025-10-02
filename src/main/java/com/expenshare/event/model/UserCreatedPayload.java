package com.expenshare.event.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UserCreatedPayload(Long userId) {
}
