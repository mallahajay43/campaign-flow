package me.mallahajay43.campaignflow.common.exceptions;

import lombok.Getter;

@Getter
public class InvalidCsvStructureException extends RuntimeException {
    public InvalidCsvStructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
