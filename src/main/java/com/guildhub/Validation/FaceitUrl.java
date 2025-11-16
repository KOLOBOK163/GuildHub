package com.guildhub.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FaceitUrlValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FaceitUrl {
    String message() default "Invalid Faceit URL format. Must be: https://www.faceit.com/en/players/username or https://faceit.com/en/players/username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

