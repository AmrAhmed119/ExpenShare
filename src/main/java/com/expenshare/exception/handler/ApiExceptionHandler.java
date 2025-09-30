package com.expenshare.exception.handler;

import com.expenshare.exception.ApiException;
import com.expenshare.exception.util.ErrorResponse;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Produces
@Singleton
@Requires(classes = {ApiException.class, ExceptionHandler.class})
public class ApiExceptionHandler implements ExceptionHandler<ApiException, HttpResponse<?>> {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Override
    public HttpResponse<?> handle(HttpRequest request, ApiException exception) {
        String requestId = MDC.get("X-Request-ID");
        if (requestId == null || requestId.isBlank()) {
            requestId = "N/A";
        }

        logger.error("Exception occurred: {}", exception.getMessage(), exception);

        return HttpResponse.status(exception.getHttpStatus()).body(new ErrorResponse(
                exception.getErrorCode(),
                exception.getMessage(),
                request.getPath(),
                requestId
        ));
    }
}
