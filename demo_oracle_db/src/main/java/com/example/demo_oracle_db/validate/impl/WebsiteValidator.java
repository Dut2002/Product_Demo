package com.example.demo_oracle_db.validate.impl;

import com.example.demo_oracle_db.validate.WebsiteConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WebsiteValidator implements ConstraintValidator<WebsiteConstraint, String> {

    private static final String WEBSITE_ADDRESS_PATTERN = "^((https|http)?://)?(www.)?[a-zA-Z0-9-]+(.[a-zA-Z]{2,})+(:\\d+)?(/.*)?$";

    @Override
    public void initialize(WebsiteConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        //check pattern phone
        return value.matches(WEBSITE_ADDRESS_PATTERN);
    }
}
