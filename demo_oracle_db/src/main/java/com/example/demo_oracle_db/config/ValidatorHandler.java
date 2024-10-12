package com.example.demo_oracle_db.config;

import com.example.demo_oracle_db.exception.EntityValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ValidatorHandler<T> {

    @Autowired
    private Validator validator;

    @Autowired
    MessageSource messageSource;

    public void validate(@NotNull T entity) throws EntityValidationException {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        Map<String, String> errors = new HashMap<>();

        violations.forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        if (!errors.isEmpty()) {
            throw new EntityValidationException(messageSource.getMessage(
                    "error.validation",
                    new Object[0],
                    LocaleContextHolder.getLocale()), errors);
        }
    }

    public Map<String, String>  validateRequest(@NotNull T entity){
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        Map<String, String> errors = new HashMap<>();

        violations.forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return errors;
    }
}
