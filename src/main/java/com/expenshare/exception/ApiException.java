package com.expenshare.exception;

import io.micronaut.http.HttpStatus;
import lombok.Getter;

/**
 * Generic domain exception for API errors.
 * This exception is used to represent domain-specific errors in the application.
 * It contains additional metadata to help clients and developers understand the error context.
 */
@Getter
public class ApiException extends RuntimeException{

    /**
     * Application-specific error code for identifying the error type.
     * Could be an enum or a predefined set of strings in a real application.
     */
    private final String errorCode;

    /**
     * HTTP status code to be returned for this exception.
     */
    private final HttpStatus httpStatus;

    /**
     * Constructs a new ApiException.
     *
     * @param details    Detailed error message
     * @param errorCode  Application-specific error code
     * @param httpStatus HTTP status code (defaults to 400 Bad Request if null)
     */
    public ApiException(String details, String errorCode, HttpStatus httpStatus) {
        super(details);
        this.errorCode = errorCode;
        this.httpStatus = (httpStatus != null) ? httpStatus : HttpStatus.BAD_REQUEST;
    }
}