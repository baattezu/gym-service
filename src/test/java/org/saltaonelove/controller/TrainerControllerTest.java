package org.saltaonelove.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.saltaonelove.InitModels;
import org.saltaonelove.TestSecurityConfig;
import org.saltaonelove.model.dto.auth.AuthResponse;
import org.saltaonelove.model.dto.trainer.TrainerRequest;
import org.saltaonelove.model.dto.trainer.TrainerResponse;
import org.saltaonelove.model.dto.trainer.TrainerUpdateRequest;
import org.saltaonelove.model.dto.training.TrainingResponse;
import org.saltaonelove.gymshared.security.service.JwtService;
import org.saltaonelove.service.CustomUserDetailsService;
import org.saltaonelove.service.TrainerService;
import org.saltaonelove.util.mapper.TrainerDtoMapper;
import org.saltaonelove.util.mapper.TrainingDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerController.class)
@ContextConfiguration(classes = TestSecurityConfig.class)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    void testRegisterTrainer() throws Exception {
        TrainerRequest request = new TrainerRequest("Jane", "Doe", "Cardio");

        when(trainerService.registerTrainer(request)).thenReturn(new AuthResponse("Jane.Doe", "somepassword"));

        mockMvc.perform(post("/api/trainer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Jane.Doe"))
                .andExpect(jsonPath("$.password").value("somepassword"));
    }

    @Test
    void testGetTrainerByUsername() throws Exception {
        TrainerResponse response = TrainerDtoMapper.toTrainerResponse(InitModels.initTrainer());

        when(trainerService.showProfile("trainer1")).thenReturn(response);

        mockMvc.perform(get("/api/trainer/trainer1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void testGetTrainerTrainings() throws Exception {
        List<TrainingResponse> trainings = List.of(TrainingDtoMapper.toTrainingResponse(InitModels.initTraining()));

        when(trainerService.getTrainerTrainings("trainer1", null, null, null, null)).thenReturn(trainings);

        mockMvc.perform(get("/api/trainer/trainer1/trainings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateTrainer() throws Exception {
        TrainerUpdateRequest request = new TrainerUpdateRequest( "Jane.Doe", "Jannice", "Doe", "Specialist", true);
        TrainerResponse response = TrainerDtoMapper.toTrainerResponse(InitModels.initTrainer());

        when(trainerService.updateTrainer("Jane.Doe", request)).thenReturn(response);

        mockMvc.perform(put("/api/trainer/trainer1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testToggleActivationTrainer() throws Exception {
        when(trainerService.toggleActivationOfAccount("trainer1")).thenReturn(InitModels.initTrainer());

        mockMvc.perform(patch("/api/trainer/trainer1/activation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
