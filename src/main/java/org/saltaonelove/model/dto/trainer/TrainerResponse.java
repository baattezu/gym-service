package org.saltaonelove.model.dto.trainer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.saltaonelove.model.dto.trainee.TraineeResponse;
import org.saltaonelove.model.entity.TrainingType;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record TrainerResponse(
        String username ,
        String firstName,
        String lastName,
        TrainingType specialization,
        Boolean isActive,
        List<TraineeResponse> traineesList
) {

}
