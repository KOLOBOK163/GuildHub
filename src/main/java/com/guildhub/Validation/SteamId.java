package com.guildhub.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = SteamIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SteamId {
    String message() default "Invalid Steam ID format. Valid formats: Steam64 ID (17 digits), Steam Community URL, or STEAM_X:Y:Z format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

