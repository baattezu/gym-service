package org.saltaonelove.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.InitModels;
import org.saltaonelove.model.entity.Trainee;
import org.saltaonelove.model.entity.Trainer;
import org.saltaonelove.util.mapper.TraineeDtoMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TraineeDtoMapperTest {

    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainee = InitModels.initTrainee();
        trainer = InitModels.initTrainer();
    }

    @Test
    void testToTraineeResponse() {
        trainee.setTrainers(List.of(trainer));

        var response = TraineeDtoMapper.toTraineeResponse(trainee);

        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals(LocalDate.of(2001, 1, 1), response.dateOfBirth());
        assertEquals("address1", response.address());
        assertTrue(response.isActive());

        assertEquals(1, response.trainersList().size());
        assertEquals("Jane.Doe", response.trainersList().get(0).username());
    }

    @Test
    void testToTraineeResponseInList() {
        var response = TraineeDtoMapper.toTraineeResponseInList(trainee);

        assertEquals("John.Doe", response.username());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
    }

}
