package com.expenshare.validation.mobile;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MobileNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface E164 {

    String message() default "Invalid mobile number, must follow E.164 format (e.g. +1234567890)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
