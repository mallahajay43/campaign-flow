package me.mallahajay43.campaignflow.common.exceptions;

import lombok.Getter;

@Getter
public class InvalidImportEventException extends RuntimeException {
    public InvalidImportEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
