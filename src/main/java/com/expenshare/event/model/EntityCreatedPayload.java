package com.expenshare.event.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record EntityCreatedPayload(Long entityId) {
}
