package me.mallahajay43.campaignflow.common.exceptions;

import lombok.Getter;

@Getter
public class ImportStorageException extends RuntimeException {

    public ImportStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
