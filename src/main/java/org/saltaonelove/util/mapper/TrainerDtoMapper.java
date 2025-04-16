package org.saltaonelove.util.mapper;

import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.model.Trainer;

public class TrainerDtoMapper {

    public static TrainerResponse toTrainerResponse(Trainer t) {
        return TrainerResponse.builder()
                .firstName(t.getFirstName())
                .lastName(t.getLastName())
                .specialization(t.getSpecialization())
                .isActive(t.isActive())
                .traineesList(t.getTrainees().stream().map(TraineeDtoMapper::toTraineeResponseInList).toList())
                .build();
    }

    public static TrainerResponse toTrainerResponseInList(Trainer t) {
        return TrainerResponse.builder()
                .username(t.getUsername())
                .firstName(t.getFirstName()).lastName(t.getLastName())
                .specialization(t.getSpecialization())
                .build();
    }
}
