package com.example.demo_oracle_db.validate.impl;

import com.example.demo_oracle_db.validate.CustomDateConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CustomDateValidator implements ConstraintValidator<CustomDateConstraint, String> {

    private String pattern;

    @Override
    public void initialize(CustomDateConstraint constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // Tạo SimpleDateFormat với định dạng tùy chỉnh
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        try {
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
