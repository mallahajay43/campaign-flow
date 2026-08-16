package me.mallahajay43.campaignflow.common.exceptions;

import lombok.Getter;

@Getter
public class ImportLeaseLostException extends RuntimeException {

    public ImportLeaseLostException(String message) {
        super(message);
    }
}
