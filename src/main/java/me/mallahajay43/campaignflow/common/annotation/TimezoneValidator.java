package me.mallahajay43.campaignflow.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimezoneValidator
        implements ConstraintValidator<ValidTimezone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String trimmed = value.trim();

        if (trimmed.isBlank()) {
            return false;
        }

        try {
            ZoneId zoneId = ZoneId.of(trimmed);

            // Explicitly allow "UTC" or check for regional format
            return "UTC".equalsIgnoreCase(trimmed)
                    || (!(zoneId instanceof ZoneOffset) && trimmed.contains("/"));

        } catch (DateTimeException ex) {
            return false;
        }
    }

}
