package org.saltaonelove.dto.training;

import org.saltaonelove.model.TrainingType;

import java.time.LocalDate;

public record TrainingResponse(
        String trainingName,
        LocalDate trainingDate,
        TrainingType trainingType,
        Long duration,
        String trainerUsername

) {
}
