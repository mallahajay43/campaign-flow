package me.mallahajay43.campaignflow.common.exceptions;

import lombok.Getter;

@Getter
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
