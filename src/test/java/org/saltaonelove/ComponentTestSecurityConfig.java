package org.saltaonelove;

import org.mockito.Mockito;
import org.saltaonelove.gymshared.security.service.JwtService;
import org.saltaonelove.gymshared.security.service.LoginAttemptService;
import org.saltaonelove.gymshared.util.auth.LoginAttemptUtils;
import org.saltaonelove.service.CustomUserDetailsService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Primary
public class ComponentTestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .anyRequest().permitAll());
        return http.build();
    }

}
