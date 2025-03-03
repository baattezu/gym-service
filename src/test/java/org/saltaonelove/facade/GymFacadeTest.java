package org.saltaonelove.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.service.TraineeService;
import org.saltaonelove.service.TrainerService;
import org.saltaonelove.service.TrainingService;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private Logger log;

    @InjectMocks
    private GymFacade gymFacade;

    private Trainee trainee;
    private Trainer trainer;
    private Training training;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setPassword("password1");

        trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setPassword("password1");

        training = new Training();
        training.setTrainerId(1L);
        training.setTraineeId(1L);
        training.setName("Strength Training");
        training.setDate(LocalDate.of(2024, 2, 1));
        training.setDuration(Duration.ofMinutes(60));
    }

    @Test
    void testRegisterTrainee() {
        when(traineeService.registerTrainee("John", "Doe")).thenReturn(trainee);

        Trainee result = gymFacade.registerTrainee("John", "Doe");

        verify(traineeService).registerTrainee("John", "Doe");
        assertEquals(trainee, result);
    }

    @Test
    void testRegisterTrainer() {
        when(trainerService.registerTrainer("John", "Doe")).thenReturn(trainer);

        Trainer result = gymFacade.registerTrainer("John", "Doe");

        verify(trainerService).registerTrainer("John", "Doe");
        assertEquals(trainer, result);
    }

    @Test
    void testRegisterTraining() {
        gymFacade.registerTraining(1L, 1L, LocalDate.of(2024, 2, 1), Duration.ofMinutes(60), "Strength Training", "Cardio");

        verify(trainingService).addTrainingType(any(Training.class), eq("Cardio"));
        verify(trainingService).createTraining(any(Training.class));
    }

    @Test
    void testUpdateTrainer() {
        TrainerDTO trainerDTO = new TrainerDTO("NewName", "NewLastName", "Strength");

        when(trainerService.updateTrainer(1L, trainerDTO)).thenReturn(trainer);

        Trainer result = gymFacade.updateTrainer(1L, trainerDTO);

        verify(trainerService).updateTrainer(1L, trainerDTO);
        assertEquals(trainer, result);
    }

    @Test
    void testUpdateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO("NewName", "NewLastName", "2001-01-01", "New Address");

        when(traineeService.updateTrainee(1L, traineeDTO)).thenReturn(trainee);

        Trainee result = gymFacade.updateTrainee(1L, traineeDTO);

        verify(traineeService).updateTrainee(1L, traineeDTO);
        assertEquals(trainee, result);
    }

    @Test
    void testShowTrainees() {
        when(traineeService.listTrainees()).thenReturn(List.of(trainee));

        gymFacade.showTrainees();

        verify(traineeService).listTrainees();
    }

    @Test
    void testShowTrainers() {
        when(trainerService.listTrainers()).thenReturn(List.of(trainer));

        gymFacade.showTrainers();

        verify(trainerService).listTrainers();
    }

    @Test
    void testShowTrainings() {
        when(trainingService.listTrainings()).thenReturn(List.of(training));

        gymFacade.showTrainings();

        verify(trainingService).listTrainings();
    }
}