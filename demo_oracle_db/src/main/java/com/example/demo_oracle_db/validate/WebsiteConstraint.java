package com.example.demo_oracle_db.validate;

import com.example.demo_oracle_db.validate.impl.WebsiteValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WebsiteValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebsiteConstraint {
    String message() default "Invalid website address format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
