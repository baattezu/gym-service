package org.saltaonelove.util.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class LoginAttemptUtils {
    public String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return xfHeader == null ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }
}