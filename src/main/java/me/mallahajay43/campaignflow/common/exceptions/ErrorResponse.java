package me.mallahajay43.campaignflow.common.exceptions;

import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String errorCode,
        String errorDescription,
        LocalDateTime timestamp,
        List<FieldError> fieldErrors
) {
    public static ErrorResponse of(String errorCode, String errorDescription, LocalDateTime timestamp) {
        return new ErrorResponse(errorCode, errorDescription, timestamp, null);
    }

    public static ErrorResponse of(String errorCode, String errorDescription, LocalDateTime timestamp, List<FieldError> fieldErrors) {
        return new ErrorResponse(errorCode, errorDescription, timestamp, fieldErrors);
    }
}