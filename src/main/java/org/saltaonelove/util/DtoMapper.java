package org.saltaonelove.util;

import org.saltaonelove.dto.trainee.TraineeResponse;
import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.dto.training.SpecializationDTO;
import org.saltaonelove.dto.training.TrainingResponse;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;

import java.time.LocalDate;

public class DtoMapper {

    public static TraineeResponse toTraineeResponse(Trainee t) {
        return TraineeResponse.builder()
                .firstName(t.getFirstName()).lastName(t.getLastName())
                .dateOfBirth(t.getDateOfBirth() != null ? t.getDateOfBirth() : LocalDate.ofYearDay(1990, 1))
                .address(t.getAddress() != null ? t.getAddress() : "")
                .isActive(t.isActive())
                .trainersList(t.getTrainers().stream().map(DtoMapper::toTrainerResponseInList).toList())
                .build();
    }

    public static TrainerResponse toTrainerResponse(Trainer t) {
        return TrainerResponse.builder()
                .firstName(t.getFirstName())
                .lastName(t.getLastName())
                .specialization(t.getSpecialization())
                .isActive(t.isActive())
                .traineesList(t.getTrainees().stream().map(DtoMapper::toTraineeResponseInList).toList())
                .build();
    }
    
    public static TrainerResponse toTrainerResponseInList(Trainer t) {
        return TrainerResponse.builder()
                .username(t.getUsername())
                .firstName(t.getFirstName()).lastName(t.getLastName())
                .specialization(t.getSpecialization())
                .build();
    }
    
    public static TraineeResponse toTraineeResponseInList(Trainee t) {
        return TraineeResponse.builder()
                .username(t.getUsername())
                .firstName(t.getFirstName()).lastName(t.getLastName())
                .build();
    }

    public static TrainingResponse toTrainingResponse(Training t) {
        return new TrainingResponse(
                t.getTrainingName(), t.getDate(),
                t.getTrainingType(), t.getDuration(),
                t.getTrainer().getUsername());
    }

}
