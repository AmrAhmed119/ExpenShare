package com.expenshare.validation.mobile;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Singleton
public class MobileNumberValidator implements ConstraintValidator<E164, String> {
    // E.164 allows up to 15 digits, starts with '+'
    private static final String E164_REGEX = "^\\+[1-9]\\d{1,14}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(E164_REGEX);
    }
}
