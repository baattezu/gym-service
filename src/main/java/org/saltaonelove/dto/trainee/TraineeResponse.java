package org.saltaonelove.dto.trainee;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.saltaonelove.dto.trainer.TrainerResponse;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record TraineeResponse(
        String username,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        Boolean isActive,
        List<TrainerResponse> trainersList
) {

}
