package org.saltaonelove.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.saltaonelove.TestSecurityConfig;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.auth.AuthResponse;
import org.saltaonelove.dto.auth.ChangeLoginRequest;
import org.saltaonelove.service.CustomUserDetailsService;
import org.saltaonelove.service.JwtService;
import org.saltaonelove.service.LoginAttemptService;
import org.saltaonelove.service.UserCredentialsService;
import org.saltaonelove.util.auth.LoginAttemptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = TestSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserCredentialsService userCredentialsService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private LoginAttemptService loginAttemptService;

    @MockBean
    private LoginAttemptUtils loginAttemptUtils;


    @Test
    void testLogin() throws Exception {
        AuthRequest authRequest = new AuthRequest("testuser", "password123");

        when(userCredentialsService.login(authRequest)).thenReturn(new AuthResponse("sometoken"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        Mockito.verify(userCredentialsService).login(authRequest);
    }

    @Test
    void testChangePassword() throws Exception {
        String username = "testuser";

        ChangeLoginRequest changeLoginRequest = new ChangeLoginRequest(
                username,
                "oldPassword",
                "newPassword");

        mockMvc.perform(put("/api/auth/change-password/" + username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeLoginRequest)))
                .andExpect(status().isOk());

        Mockito.verify(userCredentialsService).changeLogin(username, changeLoginRequest);
    }
}
