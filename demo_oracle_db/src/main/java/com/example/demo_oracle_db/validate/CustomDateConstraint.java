package com.example.demo_oracle_db.validate;

import com.example.demo_oracle_db.validate.impl.CustomDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDateConstraint {
    String message() default "Invalid date format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern();  // Định dạng ngày tùy chỉnh
}
