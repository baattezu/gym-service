package org.saltaonelove.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.saltaonelove.dto.auth.AuthRequest;

import java.time.LocalDate;

public record TrainingRequest(
        @NotNull(message = "Trainee username is required")
        String traineeUsername,
        @NotNull(message = "Trainer username is required")
        String trainerUsername,
        @NotNull(message = "Training name is required")
        String trainingName,
        @NotNull(message = "Training date is required")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate trainingDate,
        @NotNull(message = "Training duration is required")
        @Positive
        Long duration
) {
}
