package me.mallahajay43.campaignflow.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
                "INVALID_ARGUMENT_MERCHANT",
                "Invalid signup request body.",
                LocalDateTime.now(),
                ex.getBindingResult().getFieldErrors()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse.of(ex.errorCode, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.of(ex.getResourceName() + "_NOT_FOUND", ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(FileImportException.class)
    public ResponseEntity<ErrorResponse> handleFileImportException(FileImportException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.of(ex.getResourceName() + "_BAD_REQUEST", ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.of(ex.getMessage(), ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolationException(BusinessRuleViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.of(ex.getErrorCode(), ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(JwtExpiredRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolationException(JwtExpiredRefreshTokenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.of(ex.getErrorCode(), ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStateTransitionException(InvalidStateTransitionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.of("INVALID_TRANSITION", ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.of(ex.getMessage(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitException(RateLimitException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("X-RateLimit-Remaining", String.valueOf(ex.getRemaining()))
                .header("Retry-After", String.valueOf(ex.getRetryAfterSeconds()))
                .header("X-RateLimit-Reset", String.valueOf(
                        Instant.now().plusSeconds(ex.getRetryAfterSeconds()).getEpochSecond()
                ))
                .body(ErrorResponse.of("RATE_LIMIT_EXCEEDED", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AuthorizationDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ErrorResponse.of("ACCESS_DENIED", ex.getMessage(), LocalDateTime.now())
        );
    }
}
