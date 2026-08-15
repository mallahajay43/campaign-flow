package me.mallahajay43.campaignflow.common.exceptions;

import lombok.Getter;

@Getter
public class FileImportException extends RuntimeException {
    private final String resourceName;

    public FileImportException(String resourceName, String message) {
        super(message);
        this.resourceName = resourceName;
    }
}
