package me.mallahajay43.campaignflow.identity.security;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.enums.UserRole;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.identity.entity.RefreshToken;
import me.mallahajay43.campaignflow.identity.entity.User;
import me.mallahajay43.campaignflow.identity.repository.RefreshTokenRepository;
import me.mallahajay43.campaignflow.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${app.jwt.secret-key}")
    private String secret;

    @Value("${app.jwt.refreshExpirationHr:24}")
    private Long refreshTokenDurationHr;

    @Value("${app.jwt.accessExpirationMs:10}")
    private Long accessTokenDurationMs;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String email, UUID userId, UUID tenantId, String role) {
        return  Jwts.builder()
                .subject(email)
                .claim("tenantId", tenantId)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(10*60)))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims verifyJwtToken(String token) {
        return  Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getTenantId(Claims claims) {
        return claims.get("tenantId").toString();
    }

    public UserRole getRole(Claims claims) {
        return UserRole.valueOf(claims.get("role").toString());
    }

    public String getUserId(Claims claims) {
        return claims.get("userId").toString();
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    public String generateRefreshToken(String email, UUID userId, UUID tenantId, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("tenantId", tenantId)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L*3600*refreshTokenDurationHr))
                .signWith(getSecretKey())
                .compact();
    }

}
