package me.mallahajay43.campaignflow.identity.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
