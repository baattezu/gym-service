package org.saltaonelove.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.TrainingType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeDAOTest {

    @Mock
    private Storage storage;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private TrainingTypeDAO trainingTypeDAO;

    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType("Yoga");
        trainingType.setId(1L);
    }

    @Test
    void testFindAll() {
        when(storage.findAll("trainingType")).thenReturn(List.of(trainingType));

        List<TrainingType> result = trainingTypeDAO.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Yoga", result.get(0).getName());

        verify(storage).findAll("trainingType");
    }

    @Test
    void testFindById() {
        when(storage.findById("trainingType", 1L)).thenReturn(trainingType);

        TrainingType result = trainingTypeDAO.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Yoga", result.getName());

        verify(storage).findById("trainingType", 1L);
    }

    @Test
    void testSave_NewTrainingType() {
        when(idGenerator.nextId("trainingType")).thenReturn(100L);

        TrainingType newTrainingType = new TrainingType("Pilates");

        TrainingType savedTrainingType = trainingTypeDAO.save(newTrainingType);

        assertNotNull(savedTrainingType.getId());
        assertEquals(100L, savedTrainingType.getId());
        assertEquals("Pilates", savedTrainingType.getName());

        verify(idGenerator).nextId("trainingType");
        verify(storage).save("trainingType", 100L, newTrainingType);
    }

    @Test
    void testSave_ExistingTrainingType() {
        trainingTypeDAO.save(trainingType);

        verify(storage).save("trainingType", 1L, trainingType);
        verify(idGenerator, never()).nextId(anyString());
    }
}