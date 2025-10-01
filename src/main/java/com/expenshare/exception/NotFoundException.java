package com.expenshare.exception;

import io.micronaut.http.HttpStatus;

public class NotFoundException extends ApiException {
    private final static String ERROR_CODE = "NOT_FOUND";
    private final static HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public NotFoundException(String details) {
        super(details, ERROR_CODE, HTTP_STATUS);
    }
}
