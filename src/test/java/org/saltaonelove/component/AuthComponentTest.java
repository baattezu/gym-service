package org.saltaonelove.component;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.saltaonelove.InitModels;
import org.saltaonelove.TestKafkaConfig;
import org.saltaonelove.clients.workload.WorkloadClient;
import org.saltaonelove.gymshared.security.service.JwtService;
import org.saltaonelove.model.dto.auth.AuthRequest;
import org.saltaonelove.model.dto.auth.ChangeLoginRequest;
import org.saltaonelove.model.entity.Trainee;
import org.saltaonelove.model.entity.User;
import org.saltaonelove.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        TestKafkaConfig.class
})
@Transactional
public class AuthComponentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private WorkloadClient workloadClient;

    @PersistenceContext
    private EntityManager entityManager;

    private String token;

    @BeforeEach
    void setup() {
        Trainee user = InitModels.initTrainee();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Map<String, Object> roles = new HashMap<>();
        roles.put("roles", Arrays.asList("ROLE_TRAINEE"));
        token = jwtService.generateToken(roles, org.springframework.security.core.userdetails.User.builder()
                .username("John.Doe").password("password123")
                .roles("TRAINEE").build()
        );
    }

    @Test
    void testLogin() throws Exception {
        AuthRequest request = new AuthRequest("John.Doe", "password123");

        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testChangePassword() throws Exception {
        ChangeLoginRequest changeLoginRequest = new ChangeLoginRequest(
                "John.Doe", "password123", "newPassword456");

        mockMvc.perform(put("/api/auth/change-password/John.Doe")
                        .header("Authorization", "Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeLoginRequest)))
                .andExpect(status().isOk());

        User updated = userRepository.findByUsername("John.Doe").orElseThrow();
        assertTrue(passwordEncoder.matches("newPassword456", updated.getPassword()));
    }

    @Test
    void testChangePasswordWrongOld() throws Exception {
        ChangeLoginRequest changeLoginRequest = new ChangeLoginRequest(
                "John.Doe", "wrongOld", "newPassword456");

        mockMvc.perform(put("/api/auth/change-password/John.Doe")
                        .header("Authorization", "Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeLoginRequest)))
                .andExpect(status().isBadRequest());
    }
}