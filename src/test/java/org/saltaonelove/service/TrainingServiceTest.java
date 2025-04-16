package org.saltaonelove.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.training.TrainingDTO;
import org.saltaonelove.dto.training.TrainingRequest;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.TrainingRepository;
import org.saltaonelove.repos.TrainingTypeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;
    private Trainer trainer;
    private Trainee trainee;
    private AuthRequest authRequest;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainee = InitModels.initTrainee();
        trainer = InitModels.initTrainer();
        trainingType = InitModels.initTrainingType();
        training = InitModels.initTraining(trainee, trainer, trainingType);
        authRequest = new AuthRequest(trainee.getUsername(), trainee.getPassword());
    }

    @Test
    void testCreateTraining() {
        doReturn(training).when(trainingRepository).save(any(Training.class));
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(trainer));
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(trainee));

        TrainingRequest trainingRequest = new TrainingRequest(authRequest, trainee.getUsername(), trainer.getUsername(),"Cardio with Jane",
                LocalDate.of(2012,12,12), 60L);

        Training result = trainingService.createTraining(trainingRequest);
        assertNotNull(result);

        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void testListTrainings() {
        when(trainingRepository.findAll()).thenReturn(List.of(training));

        List<Training> result = trainingService.listTrainings();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(trainingRepository).findAll();
    }

    @Test
    void testGetTraining() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.ofNullable(training));

        Training result = trainingService.getTraining(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTrainingId());

        verify(trainingRepository).findById(1L);
    }

}