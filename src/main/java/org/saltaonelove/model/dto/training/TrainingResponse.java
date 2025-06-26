package org.saltaonelove.model.dto.training;

import org.saltaonelove.model.entity.TrainingType;

import java.time.LocalDate;

public record TrainingResponse(
        String trainingName,
        LocalDate trainingDate,
        TrainingType trainingType,
        Long duration,
        String trainerUsername

) {
}
