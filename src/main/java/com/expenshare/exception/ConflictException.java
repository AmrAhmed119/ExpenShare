package com.expenshare.exception;

import io.micronaut.http.HttpStatus;

public class ConflictException extends ApiException {
    private final static String ERROR_CODE = "CONFLICT";
    private final static HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public ConflictException(String details) {
        super(details, ERROR_CODE, HTTP_STATUS);
    }
}
