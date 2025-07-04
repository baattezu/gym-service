package org.saltaonelove.model.dto.training;

import java.time.LocalDate;

public record TrainingDTO (
        Long traineeId,
        Long trainerId,
        String trainingName,
        Long trainingTypeId,
        LocalDate date,
        Long duration
){}