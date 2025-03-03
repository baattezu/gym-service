package org.saltaonelove.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.dao.TrainingDAO;
import org.saltaonelove.dao.TrainingTypeDAO;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType("Yoga");

        training = new Training();
        training.setTraineeId(1L);
        training.setTrainerId(1L);
        training.setTrainingId(1L);
        training.setName("Morning Yoga");
        training.setTrainingType(trainingType);
    }

    @Test
    void testAddTrainingType_NewType() {
        when(trainingTypeDAO.findAll()).thenReturn(List.of());
        when(trainingTypeDAO.save(any(TrainingType.class))).thenReturn(trainingType);

        Training result = trainingService.addTrainingType(training, "Yoga");

        assertNotNull(result);
        assertEquals("Yoga", result.getTrainingType().getName());

        verify(trainingTypeDAO).findAll();
        verify(trainingTypeDAO).save(any(TrainingType.class));
    }

    @Test
    void testAddTrainingType_ExistingType() {
        when(trainingTypeDAO.findAll()).thenReturn(List.of(trainingType));

        Training result = trainingService.addTrainingType(training, "Yoga");

        assertNotNull(result);
        assertEquals("Yoga", result.getTrainingType().getName());

        verify(trainingTypeDAO).findAll();
        verify(trainingTypeDAO, never()).save(any(TrainingType.class));
    }

    @Test
    void testCreateTraining() {
        when(trainingDAO.save(any(Training.class))).thenReturn(training);

        Training result = trainingService.createTraining(training);

        assertNotNull(result);
        assertEquals("Morning Yoga", result.getName());

        verify(trainingDAO).save(training);
    }

    @Test
    void testListTrainings() {
        when(trainingDAO.findAll()).thenReturn(List.of(training));

        List<Training> result = trainingService.listTrainings();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(trainingDAO).findAll();
    }

    @Test
    void testGetTraining() {
        when(trainingDAO.get(1L)).thenReturn(training);

        Training result = trainingService.getTraining(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTrainingId());

        verify(trainingDAO).get(1L);
    }
}