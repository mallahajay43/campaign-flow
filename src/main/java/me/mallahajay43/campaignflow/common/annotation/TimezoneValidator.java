package me.mallahajay43.campaignflow.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimezoneValidator
        implements ConstraintValidator<ValidTimezone, String> {

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {
        // null is valid for a PATCH request because the field may be omitted
        if (value == null) {
            return true;
        }

        if (value.isBlank()) {
            return false;
        }

        try {
            ZoneId zoneId = ZoneId.of(value.trim());

            return !(zoneId instanceof ZoneOffset)
                    && value.trim().contains("/");
        } catch (DateTimeException ex) {
            return false;
        }
    }
}
