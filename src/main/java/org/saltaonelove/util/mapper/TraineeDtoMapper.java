package org.saltaonelove.util.mapper;

import org.saltaonelove.dto.trainee.TraineeResponse;
import org.saltaonelove.model.Trainee;

import java.time.LocalDate;

public class TraineeDtoMapper {

    public static TraineeResponse toTraineeResponse(Trainee t) {
        return TraineeResponse.builder()
                .firstName(t.getFirstName()).lastName(t.getLastName())
                .dateOfBirth(t.getDateOfBirth() != null ? t.getDateOfBirth() : LocalDate.ofYearDay(1990, 1))
                .address(t.getAddress() != null ? t.getAddress() : "")
                .isActive(t.isActive())
                .trainersList(t.getTrainers().stream().map(TrainerDtoMapper::toTrainerResponseInList).toList())
                .build();
    }

    public static TraineeResponse toTraineeResponseInList(Trainee t) {
        return TraineeResponse.builder()
                .username(t.getUsername())
                .firstName(t.getFirstName()).lastName(t.getLastName())
                .build();
    }

}
