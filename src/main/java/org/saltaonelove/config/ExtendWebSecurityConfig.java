package org.saltaonelove.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class ExtendWebSecurityConfig {

    private static final Map<String, String> PUBLIC_POST_ENDPOINTS = Map.of(
            "/api/trainee", "POST",
            "/api/trainer", "POST",
            "/api/auth/login", "POST"
    );

    @Bean(name = "extendedFilterChain")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain overrideSecurity(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(request ->
                        PUBLIC_POST_ENDPOINTS.containsKey(request.getRequestURI()) &&
                                request.getMethod().equalsIgnoreCase(PUBLIC_POST_ENDPOINTS.get(request.getRequestURI()))
                )
                .authorizeHttpRequests(req -> req.anyRequest().permitAll())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
