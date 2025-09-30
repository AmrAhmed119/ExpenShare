package com.expenshare.exception.handler;

import com.expenshare.exception.util.ErrorResponse;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Exception handler that overrides the default handling of ConstraintViolationException
 * in Micronaut. Provides a custom error response for validation failures, including request path and request ID.
 */
@Produces
@Singleton
@Requires(classes = {ConstraintViolationException.class, ExceptionHandler.class})
@Replaces(io.micronaut.validation.exceptions.ConstraintExceptionHandler.class)
public class ConstraintExceptionHandler implements ExceptionHandler<ConstraintViolationException, HttpResponse<?>> {

    private static final Logger logger = LoggerFactory.getLogger(ConstraintExceptionHandler.class);

    @Override
    public HttpResponse<?> handle(HttpRequest request, ConstraintViolationException exception) {
        String requestId = MDC.get("X-Request-ID");
        if (requestId == null || requestId.isBlank()) {
            requestId = "N/A";
        }

        logger.error("Exception occurred: {}", exception.getMessage(), exception);

        return HttpResponse.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
                "VALIDATION_ERROR",
                exception.getMessage(),
                request.getPath(),
                requestId
        ));
    }
}
