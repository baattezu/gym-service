package org.saltaonelove.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.InitModels;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.training.TrainingRequest;
import org.saltaonelove.model.TrainingType;
import org.saltaonelove.service.TrainingService;
import org.saltaonelove.service.UserCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainingService trainingService;

    @MockBean
    private UserCredentialsService userCredentialsService;

    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest("user1", "password1");
    }

    @Test
    void testCreateTraining() throws Exception {
        TrainingRequest request = new TrainingRequest(authRequest, "John.Doe", "Jane.Doe", "CARDIO", LocalDate.ofYearDay(2001,1), 50L);

        when(trainingService.createTraining(request)).thenReturn(InitModels.initTraining());

        mockMvc.perform(post("/api/training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTrainingTypes() throws Exception {
        List<TrainingType> trainingTypes = List.of(InitModels.initTrainingType());

        when(trainingService.getTrainingTypes()).thenReturn(trainingTypes);

        mockMvc.perform(get("/api/training/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk());
    }
}

