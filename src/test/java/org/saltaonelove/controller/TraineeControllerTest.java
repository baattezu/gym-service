package org.saltaonelove.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.saltaonelove.InitModels;
import org.saltaonelove.TestSecurityConfig;
import org.saltaonelove.dto.auth.AuthResponse;
import org.saltaonelove.dto.trainee.TraineeRegisterRequest;
import org.saltaonelove.dto.trainee.TraineeResponse;
import org.saltaonelove.dto.trainee.TraineeUpdateRequest;
import org.saltaonelove.dto.trainee.TraineeUpdateTrainersRequest;
import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.dto.training.TrainingResponse;
import org.saltaonelove.service.auth.CustomUserDetailsService;
import org.saltaonelove.service.auth.JwtService;
import org.saltaonelove.service.TraineeService;
import org.saltaonelove.util.mapper.TraineeDtoMapper;
import org.saltaonelove.util.mapper.TrainerDtoMapper;
import org.saltaonelove.util.mapper.TrainingDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TraineeController.class)
@ContextConfiguration(classes = TestSecurityConfig.class)
class TraineeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TraineeService traineeService;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    void testRegisterTrainee() throws Exception {
        TraineeRegisterRequest request = new TraineeRegisterRequest("John", "Doe",
                LocalDate.ofYearDay(2001,1), "someAddress");

        when(traineeService.registerTrainee(request)).thenReturn(new AuthResponse("John.Doe", "somepassword"));

        mockMvc.perform(post("/api/trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("John.Doe"))
                .andExpect(jsonPath("$.password").value("somepassword"));
    }

    @Test
    void testGetTraineeByUsername() throws Exception {
        TraineeResponse response = TraineeDtoMapper.toTraineeResponse(InitModels.initTrainee());

        when(traineeService.showProfile("trainee1")).thenReturn(response);

        mockMvc.perform(get("/api/trainee/trainee1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testGetTrainersAvailable() throws Exception {
        List<TrainerResponse> trainers = List.of(TrainerDtoMapper.toTrainerResponse(InitModels.initTrainer()));

        when(traineeService.getTrainersAvailableForTrainee("trainee1")).thenReturn(trainers);

        mockMvc.perform(get("/api/trainee/trainee1/trainers-available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTrainings() throws Exception {
        List<TrainingResponse> trainings = List.of(TrainingDtoMapper.toTrainingResponse(InitModels.initTraining()));

        when(traineeService.getTraineeTrainings("trainee1", null, null, null, null)).thenReturn(trainings);

        mockMvc.perform(get("/api/trainee/trainee1/trainings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateTraineeProfile() throws Exception {
        TraineeUpdateRequest request = new TraineeUpdateRequest(
                "John.Doe", "John", "Doe",
                LocalDate.ofYearDay(2001,1), "address1", true);
        TraineeResponse response = TraineeDtoMapper.toTraineeResponse(InitModels.initTrainee());

        when(traineeService.updateTrainee(request)).thenReturn(response);

        mockMvc.perform(put("/api/trainee/trainee1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateTraineeTrainers() throws Exception {
        TraineeUpdateTrainersRequest request = new TraineeUpdateTrainersRequest(java.util.List.of("trainer1"));
        List<TrainerResponse> trainers = List.of(TrainerDtoMapper.toTrainerResponse(InitModels.initTrainer()));

        when(traineeService.updateTrainerList("trainee1", request.trainersList())).thenReturn(trainers);

        mockMvc.perform(put("/api/trainee/trainee1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testToggleActivation() throws Exception {
        when(traineeService.toggleActivationOfAccount("John.Doe")).thenReturn(InitModels.initTrainee());

        mockMvc.perform(patch("/api/trainee/trainee1/activation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteTraineeProfile() throws Exception {
        Mockito.doNothing().when(traineeService).deleteTrainee("trainee1");

        mockMvc.perform(delete("/api/trainee/trainee1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
