package org.saltaonelove.model.dto.trainee;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TraineeUpdateTrainersRequest (
        @NotNull List<String> trainersList
) {}
