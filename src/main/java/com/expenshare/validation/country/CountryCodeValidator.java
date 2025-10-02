package com.expenshare.validation.country;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;

@Singleton
public class CountryCodeValidator implements ConstraintValidator<CountryCode, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.length() != 2) {
            return false;
        }

        String upper = value.toUpperCase(Locale.ROOT);

        for (String iso : Locale.getISOCountries()) {
            if (iso.equals(upper)) {
                return true;
            }
        }

        return false;
    }
}
