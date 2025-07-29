package com.ferticare.ferticareback.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrimmedNotBlankValidator implements ConstraintValidator<TrimmedNotBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return !value.trim().isEmpty();
    }
}
