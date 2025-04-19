package org.saltaonelove.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.saltaonelove.exception.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class RestAuthEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper op = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String responseMessage = op.writeValueAsString(errorResponse);
        response.getWriter().write(responseMessage);
    }
}