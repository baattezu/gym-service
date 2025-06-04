package org.saltaonelove.util.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.saltaonelove.service.auth.LoginAttemptService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private LoginAttemptService loginAttemptService;
    private HttpServletRequest request;
    private LoginAttemptUtils loginAttemptUtils;

    public AuthenticationFailureListener(LoginAttemptService loginAttemptService, HttpServletRequest request, LoginAttemptUtils loginAttemptUtils) {
        this.loginAttemptService = loginAttemptService;
        this.request = request;
        this.loginAttemptUtils = loginAttemptUtils;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String ip = loginAttemptUtils.getClientIP(request);
        loginAttemptService.loginFailed(ip);
    }
}