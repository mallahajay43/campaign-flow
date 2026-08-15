package me.mallahajay43.campaignflow.identity.service;

import me.mallahajay43.campaignflow.identity.dto.request.LoginRequest;
import me.mallahajay43.campaignflow.identity.dto.request.TenantSignupRequest;
import me.mallahajay43.campaignflow.identity.dto.response.LoginResponse;
import me.mallahajay43.campaignflow.identity.dto.response.TenantResponse;
import org.apache.coyote.BadRequestException;

public interface AuthService {
    TenantResponse signup(TenantSignupRequest tenantSignupRequest);
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse refresh(String refreshToken) throws BadRequestException;

    void removeSession(String refreshToken);
}
