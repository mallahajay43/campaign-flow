package me.mallahajay43.campaignflow.common.exceptions;

import lombok.Getter;

@Getter
public class ImportFileNotFoundException extends RuntimeException {

    public ImportFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
