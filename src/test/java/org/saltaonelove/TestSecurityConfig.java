package org.saltaonelove;

import org.mockito.Mockito;
import org.saltaonelove.gymshared.security.service.JwtService;
import org.saltaonelove.gymshared.security.service.LoginAttemptService;
import org.saltaonelove.gymshared.util.auth.LoginAttemptUtils;
import org.saltaonelove.service.CustomUserDetailsService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Primary
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return Mockito.mock(CustomUserDetailsService.class);
    }

    @Bean
    public JwtService jwtService() {
        return Mockito.mock(JwtService.class);
    }

    @Bean
    public LoginAttemptService loginAttemptService(){
        return Mockito.mock(LoginAttemptService.class);
    }

    @Bean
    public LoginAttemptUtils loginAttemptUtils(){
        return Mockito.mock(LoginAttemptUtils.class);
    }


}
