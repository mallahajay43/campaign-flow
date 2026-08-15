package me.mallahajay43.campaignflow.identity.security.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mallahajay43.campaignflow.common.context.TenantContext;
import me.mallahajay43.campaignflow.identity.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtUtil jwtUtil;
    private final TenantContext tenantContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token.
        String token = authorizationHeader.substring(7);

        try {
            Claims claims = jwtUtil.verifyJwtToken(token);

            if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                String email = jwtUtil.getEmail(claims);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + jwtUtil.getRole(claims).name())));
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Setting tenant context.
                tenantContext.setTenantId(UUID.fromString(jwtUtil.getTenantId(claims)));
                tenantContext.setUserId(UUID.fromString(jwtUtil.getUserId(claims)));
                tenantContext.setRole(jwtUtil.getRole(claims));
            }

            filterChain.doFilter(request, response);
        }
        catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
            log.warn("Invalid JWT Token, message: {}", ex.getMessage());
        }
    }
}
