package org.saltaonelove.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.Trainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerDAOTest {

    @Mock
    private Storage storage;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private TrainerDAO trainerDAO;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setSpecialization("Strength Training");
    }

    @Test
    void testSave() {
        when(idGenerator.nextId("trainer")).thenReturn(100L);

        trainer.setTrainerId(null);
        Trainer savedTrainer = trainerDAO.save(trainer);

        assertNotNull(savedTrainer.getTrainerId());
        assertEquals(100L, savedTrainer.getTrainerId());

        verify(storage).save("trainer", 100L, trainer);
    }

    @Test
    void testFindById() {
        when(storage.findById("trainer", 1L)).thenReturn(trainer);

        Trainer foundTrainer = trainerDAO.findById(1L);

        assertNotNull(foundTrainer);
        assertEquals(trainer, foundTrainer);
        assertEquals("John", foundTrainer.getFirstName());
        assertEquals("Doe", foundTrainer.getLastName());

        verify(storage).findById("trainer", 1L);
    }

    @Test
    void testDelete() {
        trainerDAO.delete(1L);
        verify(storage).delete("trainer", 1L);
    }

    @Test
    void testFindAll() {
        when(storage.findAll("trainer")).thenReturn(List.of(trainer));

        List<Trainer> trainers = trainerDAO.findAll();

        assertFalse(trainers.isEmpty());
        assertEquals(1, trainers.size());
        assertEquals(trainer, trainers.get(0));

        verify(storage).findAll("trainer");
    }
}