package me.mallahajay43.campaignflow.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimezoneValidator.class)
@Documented
public @interface ValidTimezone {

    String message() default "Invalid timezone";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
