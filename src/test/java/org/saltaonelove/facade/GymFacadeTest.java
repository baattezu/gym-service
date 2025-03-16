package org.saltaonelove.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.dto.AuthRequest;
import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.service.TraineeService;
import org.saltaonelove.service.TrainerService;
import org.saltaonelove.service.TrainingService;

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

    @InjectMocks
    private GymFacade gymFacade;

    private Trainee trainee;
    private Trainer trainer;
    private Training training;

    private AuthRequest defaultAuth;

    @BeforeEach
    void setUp() {
        trainee = InitModels.initTrainee();
        trainer = InitModels.initTrainer();
        training = InitModels.initTraining(trainee, trainer, trainer.getSpecialization());
        defaultAuth = new AuthRequest(trainee.getUsername(), trainee.getPassword());
    }

    @Test
    void testRegisterTrainee() {
        when(traineeService.registerTrainee(new TraineeDTO("John", "Doe"))).thenReturn(trainee);

        Trainee result = gymFacade.registerTrainee("John", "Doe");

        verify(traineeService).registerTrainee(new TraineeDTO("John", "Doe"));
        assertEquals(trainee, result);
    }

    @Test
    void testRegisterTrainer() {
        when(trainerService.registerTrainer(new TrainerDTO("Jane", "Doe", "Cardio"))).thenReturn(trainer);

        Trainer result = gymFacade.registerTrainer("Jane", "Doe", "Cardio");

        verify(trainerService).registerTrainer(new TrainerDTO("Jane", "Doe", "Cardio"));
        assertEquals(trainer, result);
    }

    @Test
    void testRegisterTraining() {
        gymFacade.registerTraining(2L, 1L, LocalDate.of(2001, 1, 1), 60L, "Strength Training", 1L);

        verify(trainingService).createTraining(any(org.saltaonelove.dto.TrainingDTO.class));
    }

    @Test
    void testUpdateTrainer() {
        Trainer updTrainer = trainer;
        updTrainer.setLastName("Down");

        TrainerDTO updTrainerDTO = new TrainerDTO("Jane", "Down", "Cardio");

        AuthRequest auth = new AuthRequest(trainer.getUsername(), trainee.getPassword());

        when(trainerService.updateTrainer(auth, updTrainerDTO)).thenReturn(trainer);

        Trainer result = gymFacade.updateTrainer(auth, updTrainerDTO);

        verify(trainerService).updateTrainer(auth, updTrainerDTO);
        assertEquals(trainer, result);
    }

    @Test
    void testUpdateTrainee() {
        Trainee updTrainee = trainee;
        updTrainee.setLastName("Down");
        updTrainee.setAddress("New Address");

        TraineeDTO updTraineeDTO = new TraineeDTO("John", "Down", "2001-01-01", "New Address");

        AuthRequest auth = new AuthRequest(trainee.getUsername(), trainee.getPassword());

        when(traineeService.updateTrainee(auth, updTraineeDTO)).thenReturn(updTrainee);

        Trainee result = gymFacade.updateTrainee(auth, updTraineeDTO);

        verify(traineeService).updateTrainee(auth, updTraineeDTO);
        assertEquals(trainee, result);
    }

    @Test
    void testShowTrainees() {
        when(traineeService.listTrainees(defaultAuth)).thenReturn(List.of(trainee));

        gymFacade.showTrainees(defaultAuth);

        verify(traineeService).listTrainees(defaultAuth);
    }

    @Test
    void testShowTrainers() {
        when(trainerService.listTrainers(defaultAuth)).thenReturn(List.of(trainer));

        gymFacade.showTrainers(defaultAuth);

        verify(trainerService).listTrainers(defaultAuth);
    }

    @Test
    void testShowTrainings() {
        when(trainingService.listTrainings()).thenReturn(List.of(training));

        gymFacade.showTrainings();

        verify(trainingService).listTrainings();
    }
}