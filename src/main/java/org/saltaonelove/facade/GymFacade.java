//package org.saltaonelove.facade;
//
//
//import org.saltaonelove.dto.auth.AuthRequest;
//import org.saltaonelove.dto.trainee.TraineeRequest;
//import org.saltaonelove.dto.trainer.TrainerRequest;
//import org.saltaonelove.dto.training.TrainingDTO;
//import org.saltaonelove.model.Trainee;
//import org.saltaonelove.model.Trainer;
//import org.saltaonelove.model.TrainingType;
//import org.saltaonelove.service.TraineeService;
//import org.saltaonelove.service.TrainerService;
//import org.saltaonelove.service.TrainingService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Component
//public class GymFacade {
//    private static final Logger log = LoggerFactory.getLogger(GymFacade.class);
//    private final TraineeService traineeService;
//    private final TrainerService trainerService;
//    private final TrainingService trainingService;
//
//    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
//        this.traineeService = traineeService;
//        this.trainerService = trainerService;
//        this.trainingService = trainingService;
//    }
//
//    public Trainee registerTrainee(String firstName, String lastName) {
//        return traineeService.registerTrainee(new TraineeRequest(firstName, lastName));
//    }
//
//    public Trainer registerTrainer(String firstName, String lastName, String specialization) {
//        return trainerService.registerTrainer(new TrainerRequest(firstName, lastName, specialization));
//    }
//
//    public void registerTraining(Long trainerId, Long traineeId, LocalDate date, Long duration, String description, Long trainingTypeId) {
//        trainingService.createTraining(
//                new TrainingDTO(traineeId, trainerId,
//                        description, trainingTypeId, date, duration));
//    }
//
//    public void toggleActivationForTrainee(AuthRequest authRequest) {
//        traineeService.toggleActivationOfAccount(authRequest);
//    }
//
//    public void toggleActivationForTrainer(AuthRequest authRequest) {
//        trainerService.toggleActivationOfAccount(authRequest);
//    }
//
//    public Trainer updateTrainer(AuthRequest authRequest, TrainerRequest trainerRequest) {
//        return trainerService.updateTrainer(authRequest, trainerRequest);
//    }
//
//    public Trainee updateTrainee(AuthRequest authRequest, TraineeRequest traineeRequest) {
//        return traineeService.updateTrainee(authRequest, traineeRequest);
//    }
//
//    public void showTrainees(AuthRequest authRequest) {
//        traineeService.listTrainees(authRequest).forEach(t ->
//                log.info(t.toString()));
//    }
//
//    public void showTrainers(AuthRequest authRequest) {
//        trainerService.listTrainers(authRequest).forEach(t ->
//                log.info(t.toString()));
//    }
//
//    public void showTrainings() {
//        trainingService.listTrainings().forEach(t ->
//                log.info("Training:" + t.getTrainingName() + " - " + t.getDate() + " - " + t.getDuration()));
//    }
//
//    public void showTraineeProfile(AuthRequest authRequest) {
//        log.info(traineeService.showProfile(authRequest).toString());
//    }
//
//    public void showTrainerProfile(AuthRequest authRequest) {
//        log.info(trainerService.showProfile(authRequest).toString());
//    }
//
//    public void deleteTrainee(AuthRequest authRequest, String traineeUsername) {
//        traineeService.deleteTrainee(authRequest, traineeUsername);
//    }
//
//    public List<TrainingType> getTrainingTypes() {
//        return trainingService.getTrainingTypes();
//    }
//
//    public void getTraineeTrainingByCriteria(AuthRequest authRequest, LocalDate from, LocalDate to, String trainerName, String trainingType) {
//        traineeService.getTraineeTrainings(authRequest, from, to, trainerName, trainingType).forEach(
//                t -> log.info(t.toString())
//        );
//    }
//
//    public void getTrainerTrainingByCriteria(AuthRequest authRequest, LocalDate from, LocalDate to, String traineeName, String trainingType) {
//        trainerService.getTrainerTrainings(authRequest, from, to, traineeName, trainingType).forEach(
//                t -> log.info(t.toString())
//        );
//    }
//
//    public void changeTraineePassword(AuthRequest authRequest, String newPassword) {
//        traineeService.changePassword(authRequest, newPassword);
//    }
//
//    public void changeTrainerPassword(AuthRequest authRequest, String newPassword) {
//        trainerService.changePassword(authRequest, newPassword);
//    }
//
//}
