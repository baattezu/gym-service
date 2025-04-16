package org.saltaonelove.dto.trainee;

import jakarta.validation.constraints.NotNull;
import org.saltaonelove.dto.auth.AuthRequest;

import java.util.List;

public record TraineeUpdateTrainersRequest (
        AuthRequest auth,
        @NotNull List<String> trainersList
        ){
}
