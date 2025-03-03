package org.saltaonelove.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.dao.TrainerDAO;
import org.saltaonelove.dao.UserDAO;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.User;
import org.saltaonelove.util.JsonLoader;
import org.saltaonelove.util.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private JsonLoader jsonLoader;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "Doe");
        user.setUserId(1L);

        trainer = UserMapper.userToTrainer(user);
        trainer.setTrainerId(1L);
        trainer.setSpecialization("Strength Training");
    }

    @Test
    void testRegisterTrainer() {
        when(userDAO.save(any(User.class))).thenReturn(user);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(trainer);

        Trainer result = trainerService.registerTrainer("John", "Doe");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(userDAO).save(any(User.class));
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void testRegisterTrainerWithSpecialization() {
        when(userDAO.save(any(User.class))).thenReturn(user);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(trainer);

        Trainer result = trainerService.registerTrainer("John", "Doe", "Strength Training");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("Strength Training", result.getSpecialization());

        verify(userDAO).save(any(User.class));
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void testUpdateTrainer() {
        TrainerDTO trainerDTO = new TrainerDTO("NewName", "NewLastName", "Cardio");

        when(trainerDAO.findById(1L)).thenReturn(trainer);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(trainer);

        Trainer result = trainerService.updateTrainer(1L, trainerDTO);

        assertNotNull(result);
        assertEquals("NewName", result.getFirstName());
        assertEquals("NewLastName", result.getLastName());
        assertEquals("Cardio", result.getSpecialization());

        verify(trainerDAO).findById(1L);
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void testListTrainers() {
        when(trainerDAO.findAll()).thenReturn(List.of(trainer));

        List<Trainer> result = trainerService.listTrainers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(trainerDAO).findAll();
    }
}