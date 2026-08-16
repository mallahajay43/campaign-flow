package me.mallahajay43.campaignflow.common.exceptions;

import lombok.Getter;

@Getter
public class EmailDeliveryException extends RuntimeException {

    public EmailDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
