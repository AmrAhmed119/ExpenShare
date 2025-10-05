package com.expenshare.exception;

import io.micronaut.http.HttpStatus;

public class ValidationException extends ApiException {
    private final static String ERROR_CODE = "VALIDATION_ERROR";
    private final static HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public ValidationException(String details) {
        super(details, ERROR_CODE, HTTP_STATUS);
    }
}
