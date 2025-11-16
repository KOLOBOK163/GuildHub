package com.guildhub.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class FaceitUrlValidator implements ConstraintValidator<FaceitUrl, String> {

    // Faceit URL pattern: https://www.faceit.com/en/players/username or https://faceit.com/en/players/username
    // Username can contain letters, numbers, hyphens, and underscores
    private static final Pattern FACEIT_URL_PATTERN = Pattern.compile(
        "^https?://(www\\.)?faceit\\.com/en/players/[a-zA-Z0-9_-]+/?$",
        Pattern.CASE_INSENSITIVE
    );

    @Override
    public void initialize(FaceitUrl constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String faceitUrl, ConstraintValidatorContext context) {
        if (faceitUrl == null || faceitUrl.trim().isEmpty()) {
            return true; // Allow null/empty values, use @NotNull if required
        }

        String trimmed = faceitUrl.trim();
        return FACEIT_URL_PATTERN.matcher(trimmed).matches();
    }
}

