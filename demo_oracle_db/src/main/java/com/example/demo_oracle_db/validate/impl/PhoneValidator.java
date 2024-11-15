package com.example.demo_oracle_db.validate.impl;

import com.example.demo_oracle_db.validate.PhoneConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneConstraint, String> {

    private static final String PHONE_PATTERN = "^(0|\\+\\d{1,3})\\d{9,11}$";

    @Override
    public void initialize(PhoneConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        //check pattern phone
        return value.matches(PHONE_PATTERN);
    }
}
