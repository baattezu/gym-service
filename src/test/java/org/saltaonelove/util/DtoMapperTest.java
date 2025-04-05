package org.saltaonelove.util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.InitModels;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DtoMapperTest {

    private Trainee trainee;
    private Trainer trainer;
    private Training training;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainee = InitModels.initTrainee();
        trainer = InitModels.initTrainer();
        trainingType = InitModels.initTrainingType();
        training = InitModels.initTraining(trainee, trainer, trainingType);
    }

    @Test
    void testToTraineeResponse() {
        trainee.setTrainers(List.of(trainer));

        var response = DtoMapper.toTraineeResponse(trainee);

        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals(LocalDate.of(2001, 1, 1), response.dateOfBirth());
        assertEquals("address1", response.address());
        assertTrue(response.isActive());

        assertEquals(1, response.trainersList().size());
        assertEquals("Jane.Doe", response.trainersList().get(0).username());
    }

    @Test
    void testToTrainerResponse() {
        trainer.setTrainees(List.of(trainee));

        var response = DtoMapper.toTrainerResponse(trainer);

        assertEquals("Jane", response.firstName());
        assertEquals("Doe", response.lastName());
        assertTrue(response.isActive());

        assertEquals(1, response.traineesList().size());
        assertEquals("John.Doe", response.traineesList().get(0).username());
    }

    @Test
    void testToTrainingResponse() {
        var response = DtoMapper.toTrainingResponse(training);

        assertEquals("Cardio Training", response.trainingName());
        assertEquals(LocalDate.of(2012, 12, 12), response.trainingDate());
        assertEquals(60L, response.duration());
        assertEquals("Jane.Doe", response.trainerUsername());
    }

    @Test
    void testToTraineeResponseInList() {
        var response = DtoMapper.toTraineeResponseInList(trainee);

        assertEquals("John.Doe", response.username());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
    }

    @Test
    void testToTrainerResponseInList() {
        var response = DtoMapper.toTrainerResponseInList(trainer);

        assertEquals("Jane.Doe", response.username());
        assertEquals("Jane", response.firstName());
        assertEquals("Doe", response.lastName());
    }
}
