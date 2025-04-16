package org.saltaonelove.util.mapper;

import org.saltaonelove.dto.training.TrainingResponse;
import org.saltaonelove.model.Training;

public class TrainingDtoMapper {

    public static TrainingResponse toTrainingResponse(Training t) {
        return new TrainingResponse(
                t.getTrainingName(), t.getDate(),
                t.getTrainingType(), t.getDuration(),
                t.getTrainer().getUsername());
    }

}
