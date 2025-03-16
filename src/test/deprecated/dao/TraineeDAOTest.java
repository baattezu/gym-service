package org.saltaonelove.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.Trainee;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeDAOTest {

    @Mock
    private Storage storage;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private TraineeDAO traineeDAO;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee("John", "Doe");
        trainee.setUserId(1L);
        trainee.setTraineeId(1L);
        trainee.setAddress("Birmingham");
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testSave() {
        when(idGenerator.nextId("trainee")).thenReturn(1L);

        trainee.setTraineeId(null);
        Trainee savedTrainee = traineeDAO.save(trainee);

        assertNotNull(savedTrainee.getTraineeId());
        assertEquals(1L, savedTrainee.getTraineeId());
        verify(storage).save("trainee", 1L, trainee);
    }

    @Test
    void testFindById() {
        when(storage.findById("trainee", 1L)).thenReturn(trainee);

        Trainee foundTrainee = traineeDAO.findById(1L);

        assertNotNull(foundTrainee);
        assertEquals(1L, foundTrainee.getTraineeId());
        verify(storage).findById("trainee", 1L);
    }

    @Test
    void testDelete() {
        traineeDAO.delete(1L);
        verify(storage).delete("trainee", 1L);
    }

    @Test
    void testFindAll() {
        when(storage.findAll("trainee")).thenReturn(List.of(trainee));

        List<Trainee> trainees = traineeDAO.findAll();

        assertFalse(trainees.isEmpty());
        assertEquals(1, trainees.size());
        verify(storage).findAll("trainee");
    }
}