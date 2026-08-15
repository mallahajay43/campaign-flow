package me.mallahajay43.campaignflow.common.exceptions;

import lombok.Getter;

@Getter
public class JwtExpiredRefreshTokenException extends RuntimeException {
    String errorCode;

    public JwtExpiredRefreshTokenException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
