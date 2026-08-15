package me.mallahajay43.campaignflow.identity.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.identity.dto.request.LoginRequest;
import me.mallahajay43.campaignflow.identity.dto.request.TenantSignupRequest;
import me.mallahajay43.campaignflow.identity.dto.response.LoginResponse;
import me.mallahajay43.campaignflow.identity.dto.response.TenantResponse;
import me.mallahajay43.campaignflow.identity.service.AuthService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<TenantResponse> signup(@Valid @RequestBody TenantSignupRequest tenantSignupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signup(tenantSignupRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue("refresh-token") String refreshToken) throws BadRequestException {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refresh-token", required = false) String refreshToken,
                                       HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        if (refreshToken != null) {
            authService.removeSession(refreshToken);

            Cookie cookie = new Cookie("refresh-token", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            httpResponse.addCookie(cookie);
        }


        return ResponseEntity.noContent().build();
    }
}
