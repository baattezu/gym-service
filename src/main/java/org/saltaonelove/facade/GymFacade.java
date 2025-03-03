package org.saltaonelove.facade;


import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.service.TraineeService;
import org.saltaonelove.service.TrainerService;
import org.saltaonelove.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

@Component
public class GymFacade {
    private static final Logger log = LoggerFactory.getLogger(GymFacade.class);
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainee registerTrainee(String firstName, String lastName) {
        return traineeService.registerTrainee(firstName, lastName);
    }

    public Trainer registerTrainer(String firstName, String lastName) {
        return trainerService.registerTrainer(firstName, lastName);
    }

    public void registerTraining(Long trainerId, Long traineeId, LocalDate date, Duration duration, String description, String trainingType) {
        Training training = new Training();
        training.setTraineeId(traineeId);
        training.setTrainerId(trainerId);
        training.setName(description);
        training.setDate(date);
        training.setDuration(duration);
        trainingService.addTrainingType(training, trainingType);
        trainingService.createTraining(training);
    }

    public Trainer updateTrainer(Long trainerId, TrainerDTO trainerDto) {
        return trainerService.updateTrainer(trainerId, trainerDto);
    }

    public Trainee updateTrainee(Long traineeId, TraineeDTO traineeDto) {
        return traineeService.updateTrainee(traineeId, traineeDto);
    }

    public void showTrainees() {
        traineeService.listTrainees().forEach(t ->
                log.info("Trainee:" + t.getUsername() + " - " + t.getPassword()));
    }

    public void showTrainers() {
        trainerService.listTrainers().forEach(t ->
                log.info("Trainer:" + t.getUsername() + " - " + t.getPassword()));
    }


    public void showTrainings() {
        trainingService.listTrainings().forEach(t ->
                log.info("Training:" + t.getName() + " - " + t.getDate() + " - " + t.getDuration()));
    }

    public void deleteTrainee(Long traineeId) {
        traineeService.deleteTrainee(traineeId);
    }
}
