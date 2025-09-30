package com.expenshare.exception.handler;

import com.expenshare.exception.util.ErrorResponse;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * This class acts as a fallback exception handler in Micronaut.
 * It handles any thrown exception (Throwable) that does not have a more specific handler.
 * This ensures that unexpected errors are caught and a consistent error response is returned.
 */
@Produces
@Singleton
@Requires(classes = {Throwable.class, ExceptionHandler.class})
public class GenericExceptionHandler implements ExceptionHandler<Throwable, HttpResponse<?>> {

    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @Override
    public HttpResponse<?> handle(HttpRequest request, Throwable exception) {
        String requestId = MDC.get("X-Request-ID");
        if (requestId == null || requestId.isBlank()) {
            requestId = "N/A";
        }

        logger.error("Exception occurred: {}", exception.getMessage(), exception);

        return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "GENERIC_ERROR",
                "An unexpected error occurred.",
                request.getPath(),
                requestId
        ));
    }
}
