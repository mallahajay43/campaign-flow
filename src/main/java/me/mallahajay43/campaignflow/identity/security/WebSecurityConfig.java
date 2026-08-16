package me.mallahajay43.campaignflow.identity.security;

import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.identity.security.filter.JwtAuthFilter;
import me.mallahajay43.campaignflow.identity.service.impl.TenantUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] PUBLIC_ROUTES = {"/v1/auth/**", "/swagger-ui/**", "/v1/api-docs/**", "/swagger-ui.html"};
    private static final String[] ADMIN_ROUTES = {"/api/v1/campaigns/**", "/api/v1/email-templates/**", "/api/v1/contact-imports/**"};

    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ROUTES).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_ROUTES).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, PUBLIC_ROUTES).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, PUBLIC_ROUTES).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(TenantUserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
