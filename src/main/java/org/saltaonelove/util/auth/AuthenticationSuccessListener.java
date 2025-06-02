package org.saltaonelove.util.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.saltaonelove.service.LoginAttemptService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private LoginAttemptService loginAttemptService;
    private HttpServletRequest request;
    private LoginAttemptUtils loginAttemptUtils;

    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService, HttpServletRequest request, LoginAttemptUtils loginAttemptUtils) {
        this.loginAttemptService = loginAttemptService;
        this.request = request;
        this.loginAttemptUtils = loginAttemptUtils;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String ip = loginAttemptUtils.getClientIP(request);
        loginAttemptService.loginSucceeded(ip);
    }
}
