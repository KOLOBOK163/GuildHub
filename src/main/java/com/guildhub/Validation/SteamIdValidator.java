package com.guildhub.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SteamIdValidator implements ConstraintValidator<SteamId, String> {

    // Steam64 ID: 17-digit number starting with 76561
    private static final Pattern STEAM64_ID_PATTERN = Pattern.compile("^7656119[0-9]{10}$");
    
    // Steam Community URL: https://steamcommunity.com/profiles/7656119... or /id/customname
    private static final Pattern STEAM_URL_PATTERN = Pattern.compile(
        "^https?://(www\\.)?steamcommunity\\.com/(profiles/7656119[0-9]{10}|id/[a-zA-Z0-9_-]+)/?$",
        Pattern.CASE_INSENSITIVE
    );
    
    // Old format: STEAM_X:Y:Z where X is universe (0 or 1), Y and Z are account numbers
    private static final Pattern STEAM_OLD_PATTERN = Pattern.compile("^STEAM_[01]:[01]:[0-9]+$", Pattern.CASE_INSENSITIVE);
    
    // Steam3 format: [U:1:XXXXXXX]
    private static final Pattern STEAM3_PATTERN = Pattern.compile("^\\[U:[01]:[0-9]+\\]$");

    @Override
    public void initialize(SteamId constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String steamId, ConstraintValidatorContext context) {
        if (steamId == null || steamId.trim().isEmpty()) {
            return true; // Allow null/empty values, use @NotNull if required
        }

        String trimmed = steamId.trim();
        
        // Check Steam64 ID (17 digits)
        if (STEAM64_ID_PATTERN.matcher(trimmed).matches()) {
            return true;
        }
        
        // Check Steam Community URL
        if (STEAM_URL_PATTERN.matcher(trimmed).matches()) {
            return true;
        }
        
        // Check old STEAM_X:Y:Z format
        if (STEAM_OLD_PATTERN.matcher(trimmed).matches()) {
            return true;
        }
        
        // Check Steam3 format
        if (STEAM3_PATTERN.matcher(trimmed).matches()) {
            return true;
        }
        
        return false;
    }
}

