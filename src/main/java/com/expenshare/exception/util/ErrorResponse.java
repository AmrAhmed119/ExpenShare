package com.expenshare.exception.util;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

/**
 * Represents the structure of an error response returned to the client.
 * Used by exception handlers to provide consistent error information.
 */
@Introspected
@Serdeable
public record ErrorResponse(
        String errorCode,
        String message,
        String path,
        String requestId
) {}
