package org.saltaonelove.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TrainingDAOTest {

    @Mock
    private Storage storage;
    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private TrainingDAO trainingDAO;

    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training();
        training.setTrainingId(1L);
        training.setTrainerId(1L);
        training.setTraineeId(1L);
        training.setName("Training Name");
        training.setDate(LocalDate.of(2020, 1, 1));
        training.setDuration(Duration.ofMinutes(50));
        training.setTrainingType(new TrainingType("Yoga"));
    }

    @Test
    void testSave() {
        when(idGenerator.nextId("training")).thenReturn(100L);

        training.setTrainingId(null);
        Training savedTraining = trainingDAO.save(training);

        assertNotNull(savedTraining.getTrainingId());
        assertEquals(100L, savedTraining.getTrainingId());

        verify(storage).save("training", 100L, training);
    }

    @Test
    void testGet() {
        when(storage.findById("training", 100L)).thenReturn(training);

        Training foundTraining = trainingDAO.get(100L);

        assertNotNull(foundTraining);
        assertEquals(training, foundTraining);
        verify(storage).findById("training", 100L);
    }

    @Test
    void testDelete() {
        trainingDAO.delete(100L);
        verify(storage).delete("training", 100L);
    }

    @Test
    void testFindAll() {
        when(storage.findAll("training")).thenReturn(List.of(training));

        List<Training> trainings = trainingDAO.findAll();

        assertFalse(trainings.isEmpty());
        assertEquals(1, trainings.size());
        verify(storage).findAll("training");
    }
}