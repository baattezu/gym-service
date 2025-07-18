package org.saltaonelove.unit.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.InitModels;
import org.saltaonelove.model.entity.Trainee;
import org.saltaonelove.model.entity.Trainer;
import org.saltaonelove.util.mapper.TrainerDtoMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainerDtoMapperTest {

    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainee = InitModels.initTrainee();
        trainer = InitModels.initTrainer();
    }

    @Test
    void testToTrainerResponse() {
        trainer.setTrainees(List.of(trainee));

        var response = TrainerDtoMapper.toTrainerResponse(trainer);

        assertEquals("Jane", response.firstName());
        assertEquals("Doe", response.lastName());
        assertTrue(response.isActive());

        assertEquals(1, response.traineesList().size());
        assertEquals("John.Doe", response.traineesList().get(0).username());
    }

    @Test
    void testToTrainerResponseInList() {
        var response = TrainerDtoMapper.toTrainerResponseInList(trainer);

        assertEquals("Jane.Doe", response.username());
        assertEquals("Jane", response.firstName());
        assertEquals("Doe", response.lastName());
    }
}
