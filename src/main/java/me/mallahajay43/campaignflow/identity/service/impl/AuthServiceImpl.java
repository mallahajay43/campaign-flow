package me.mallahajay43.campaignflow.identity.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.enums.TenantStatus;
import me.mallahajay43.campaignflow.common.enums.UserRole;
import me.mallahajay43.campaignflow.common.enums.UserStatus;
import me.mallahajay43.campaignflow.common.exceptions.DuplicateResourceException;
import me.mallahajay43.campaignflow.common.exceptions.JwtExpiredRefreshTokenException;
import me.mallahajay43.campaignflow.common.exceptions.ResourceNotFoundException;
import me.mallahajay43.campaignflow.identity.dto.request.LoginRequest;
import me.mallahajay43.campaignflow.identity.dto.request.TenantSignupRequest;
import me.mallahajay43.campaignflow.identity.dto.response.LoginResponse;
import me.mallahajay43.campaignflow.identity.dto.response.TenantResponse;
import me.mallahajay43.campaignflow.identity.entity.RefreshToken;
import me.mallahajay43.campaignflow.identity.entity.Tenant;
import me.mallahajay43.campaignflow.identity.entity.User;
import me.mallahajay43.campaignflow.identity.mapper.TenantMapper;
import me.mallahajay43.campaignflow.identity.repository.RefreshTokenRepository;
import me.mallahajay43.campaignflow.identity.repository.TenantRepository;
import me.mallahajay43.campaignflow.identity.repository.UserRepository;
import me.mallahajay43.campaignflow.identity.security.JwtUtil;
import me.mallahajay43.campaignflow.identity.service.AuthService;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Override
    public TenantResponse signup(TenantSignupRequest request) {
        if (tenantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL",
                    "Merchant account with email " + request.email() + " already exists");
        }

        Tenant tenant = tenantMapper.toEntityFromTenantSignupRequest(request);
        tenant.setStatus(TenantStatus.ACTIVE);
        if (request.timezone() != null && !request.timezone().isEmpty()) {
            tenant.setTimezone(request.timezone());
        }
        tenant = tenantRepository.save(tenant);

        User user = User.builder()
                .email(request.email())
                .passwordHash(bCryptPasswordEncoder.encode(request.password()))
                .tenant(tenant)
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .fullName(request.fullName())
                .build();
        user = userRepository.save(user);

        return tenantMapper.toResponse(tenant);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new ResourceNotFoundException("USER", loginRequest.email()));

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId(), user.getTenant().getId(), user.getRole().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getId(), user.getTenant().getId(), user.getRole().toString());

        addTokenSession(user, refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public LoginResponse refresh(String token) throws BadRequestException {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("REFRESH_TOKEN", token));

        try {
            Claims claims = jwtUtil.verifyJwtToken(token);
        } catch (ExpiredJwtException e) {
            removeSession(token);
            throw new JwtExpiredRefreshTokenException("REFRESH_TOKEN_EXPIRED", "The refresh token has expired.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadRequestException("INVALID_REFRESH_TOKEN: The token signature is invalid or tampered with.");
        }

        User user = refreshToken.getUser();

        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getId(),
                user.getTenant().getId(),
                user.getRole().toString()
        );

        refreshToken.setLastUsedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
        return new LoginResponse(accessToken, token);
    }

    public void removeSession(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->  new ResourceNotFoundException("REFRESH_TOKEN", token));

        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    private void addTokenSession(User user, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser_Id(user.getId()).orElse(null);

        if (refreshToken != null) {
            refreshToken.setLastUsedAt(LocalDateTime.now());
            refreshToken.setToken(token);
            refreshTokenRepository.save(refreshToken);
            return;
        }

        RefreshToken refreshToken1 = RefreshToken.builder()
                .token(token)
                .user(user)
                .lastUsedAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(refreshToken1);
    }
}
