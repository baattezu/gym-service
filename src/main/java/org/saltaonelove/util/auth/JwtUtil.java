package org.saltaonelove.util.auth;

import io.jsonwebtoken.Jwt;
import org.saltaonelove.config.security.JwtAuthToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUtil {

    public static String getJwtTokenFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthToken jwtAuth) {
            return jwtAuth.getToken();
        }

        return "";
    }
}
