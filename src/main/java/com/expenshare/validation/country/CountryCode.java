package com.expenshare.validation.country;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryCodeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CountryCode {

    String message() default "Invalid country code. Must be ISO 3166-1 alpha-2 (e.g., SA, EG)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
