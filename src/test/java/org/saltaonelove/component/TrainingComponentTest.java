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
import org.saltaonelove.model.dto.trainee.TraineeUpdateRequest;
import org.saltaonelove.model.entity.Trainee;
import org.saltaonelove.repos.TrainingTypeRepository;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class TrainingComponentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

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
    void testGetTrainingsWithoutFilters() throws Exception {
        mockMvc.perform(get("/api/trainee/John.Doe/trainings")
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isOk());
    }

    @Test
    void testTraineeProfileAccess() throws Exception {
        mockMvc.perform(get("/api/trainee/John.Doe")
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateTraineeProfile() throws Exception {
        TraineeUpdateRequest updateRequest = new TraineeUpdateRequest("John.Doe","John", "Doe", LocalDate.of(2001, 11,11), "Lenina 36", true );

        mockMvc.perform(put("/api/trainee/John.Doe")
                        .header("Authorization", "Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }
}