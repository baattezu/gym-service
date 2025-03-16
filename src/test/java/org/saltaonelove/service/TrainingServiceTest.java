package org.saltaonelove.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.dto.TrainingDTO;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;
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
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        training = InitModels.initTraining();
    }

    @Test
    void testCreateTraining() {
        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        Training result = trainingService.createTraining(new TrainingDTO(1L,2L, "Cardio with Jane", 1L, LocalDate.of(2012,12,12), 60L));

        assertNotNull(result);
        assertEquals("Cardio with Jane", result.getTrainingName());

        verify(trainingRepository).save(training);
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