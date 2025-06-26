package org.saltaonelove.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.InitModels;
import org.saltaonelove.model.entity.Trainee;
import org.saltaonelove.model.entity.Trainer;
import org.saltaonelove.model.entity.Training;
import org.saltaonelove.model.entity.TrainingType;
import org.saltaonelove.util.mapper.TrainingDtoMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingDtoMapperTest {

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
    void testToTrainingResponse() {
        var response = TrainingDtoMapper.toTrainingResponse(training);

        assertEquals("Cardio Training", response.trainingName());
        assertEquals(LocalDate.of(2012, 12, 12), response.trainingDate());
        assertEquals(60L, response.duration());
        assertEquals("Jane.Doe", response.trainerUsername());
    }
}
