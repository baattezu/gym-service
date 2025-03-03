package org.saltaonelove.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.dao.TraineeDAO;
import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.model.Trainee;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private UserCredentialsService userUtil;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee("John", "Doe");
        trainee.setUserId(1L);
        trainee.setTraineeId(1L);
        trainee.setAddress("Some Address");
        trainee.setDateOfBirth(LocalDate.of(1995, 5, 15));
    }

    @Test
    void testRegisterTrainee() {
        when(userUtil.generateUsername(any(Trainee.class))).thenReturn("John.Doe");
        when(traineeDAO.save(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.registerTrainee("John", "Doe");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(traineeDAO).save(any(Trainee.class));
    }

    @Test
    void testRegisterTraineeWithDetails() {
        when(userUtil.generateUsername(any(Trainee.class))).thenReturn("John.Doe");
        when(traineeDAO.save(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.registerTrainee("John", "Doe", LocalDate.of(1995, 5, 15), "Some Address");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("Some Address", result.getAddress());
        assertEquals(LocalDate.of(1995, 5, 15), result.getDateOfBirth());

        verify(traineeDAO).save(any(Trainee.class));
    }

    @Test
    void testListTrainees() {
        when(traineeDAO.findAll()).thenReturn(List.of(trainee));

        List<Trainee> result = traineeService.listTrainees();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(traineeDAO).findAll();
    }

    @Test
    void testGetTraineeById() {
        when(traineeDAO.findById(1L)).thenReturn(trainee);

        Trainee result = traineeService.getTraineeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTraineeId());

        verify(traineeDAO).findById(1L);
    }

    @Test
    void testUpdateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO("NewName", "NewLastName", "2000-01-01", "New Address");

        when(traineeDAO.findById(1L)).thenReturn(trainee);
        when(traineeDAO.save(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.updateTrainee(1L, traineeDTO);

        assertNotNull(result);
        assertEquals("NewName", result.getFirstName());
        assertEquals("NewLastName", result.getLastName());
        assertEquals("New Address", result.getAddress());
        assertEquals(LocalDate.of(2000, 1, 1), result.getDateOfBirth());

        verify(traineeDAO).findById(1L);
        verify(traineeDAO).save(any(Trainee.class));
    }
}