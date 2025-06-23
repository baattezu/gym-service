package org.saltaonelove.service;

import org.saltaonelove.dto.auth.AuthResponse;
import org.saltaonelove.dto.trainee.TraineeRegisterRequest;
import org.saltaonelove.dto.trainee.TraineeResponse;
import org.saltaonelove.dto.trainee.TraineeUpdateRequest;
import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.dto.training.TrainingResponse;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.util.UpdateUtil;
import org.saltaonelove.util.logging.LoggingUtil;
import org.saltaonelove.util.logging.annotation.TransactionalWithLogging;
import org.saltaonelove.util.mapper.TraineeDtoMapper;
import org.saltaonelove.util.mapper.TrainerDtoMapper;
import org.saltaonelove.util.mapper.TrainingDtoMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TraineeService {

    private static final LoggingUtil log = LoggingUtil.getLogger(TraineeService.class);

    private TraineeRepository traineeRepository;
    private TrainerRepository trainerRepository;
    private UserCredentialsService userUtil;
    private PasswordEncoder passwordEncoder;

    public TraineeService(TraineeRepository traineeRepository, TrainerRepository trainerRepository, UserCredentialsService userUtil, PasswordEncoder passwordEncoder) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.userUtil = userUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @TransactionalWithLogging
    public AuthResponse registerTrainee(TraineeRegisterRequest traineeRegisterRequest) {
        log.info("Registering trainee: {} {}", traineeRegisterRequest.firstName(), traineeRegisterRequest.lastName());
        Trainee trainee = new Trainee(
                traineeRegisterRequest.firstName(), traineeRegisterRequest.lastName(),
                traineeRegisterRequest.dateOfBirth(),
                traineeRegisterRequest.address()
        );
        String username = userUtil.generateUsername(trainee);
        String password = userUtil.generateRandomPassword();
        trainee.setUsername(username);
        trainee.setPassword(passwordEncoder.encode(password));
        trainee = traineeRepository.save(trainee);
        log.info("Trainer {} successfully registered", trainee.getUsername());
        return new AuthResponse(username, password);
    }

    @TransactionalWithLogging
    public Trainee toggleActivationOfAccount(String username) {
        Trainee trainee = traineeRepository.findByUsername(username).get();
        trainee.setActive(!trainee.isActive());
        trainee = traineeRepository.save(trainee);
        log.info("Trainee {} is {}", trainee.getUsername(), trainee.isActive() ? "activated" : "deactivated");
        return trainee;
    }


    public List<Trainee> listTrainees() {
        return traineeRepository.findAll();
    }

    public TraineeResponse showProfile(String username) {
        log.info("Showing profile for user: {}", username);
        Trainee t = traineeRepository.findByUsername(username).get();
        return TraineeDtoMapper.toTraineeResponse(t);
    }

    @TransactionalWithLogging
    public TraineeResponse updateTrainee(TraineeUpdateRequest traineeRequest) {
        log.info("Updating trainee: {} {}", traineeRequest.firstName(), traineeRequest.lastName());
        Trainee t = traineeRepository.findByUsername(traineeRequest.username()).get();

        t.setFirstName(traineeRequest.firstName());
        t.setLastName(traineeRequest.lastName());
        t.setActive(traineeRequest.isActive());

        UpdateUtil.setIfNotNull(traineeRequest.address(), t::setAddress);
        UpdateUtil.setIfNotNull(traineeRequest.dateOfBirth(), t::setDateOfBirth);

        t = traineeRepository.save(t);
        log.info("Updated trainee {}'s profile successfully!", t.getUsername());

        return TraineeDtoMapper.toTraineeResponse(t);
    }

    public List<TrainingResponse> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        return traineeRepository.findTraineeTrainingsByUsernameAndCriteria(
                username, fromDate, toDate, trainerName, trainingType
        ).stream().map(TrainingDtoMapper::toTrainingResponse).toList();
    }

    public List<TrainerResponse> getTrainersAvailableForTrainee(String traineeName){
        log.info("Fetching available trainers for trainee {}", traineeName);
        List<Trainer> trainers = trainerRepository.findTrainersThatAreNotAssignedToTrainee(traineeName);
        return trainers.stream().map(TrainerDtoMapper::toTrainerResponseInList).toList();
    }

    @TransactionalWithLogging
    public Trainee changePassword(String username, String newPassword) {
        log.info("User {} is attempting to change their password", username);
        Trainee trainee = traineeRepository.findByUsername(username).get();
        if (trainee.getPassword().equals(newPassword) || newPassword.length() < 10) {
            throw new IllegalArgumentException("New password repeats old password match or new password is too short");
        }
        trainee.setPassword(newPassword);

        trainee = traineeRepository.save(trainee);
        log.info("Trainee {} changed password successfully", username);
        return trainee;
    }

    @TransactionalWithLogging
    public List<TrainerResponse> updateTrainerList(String username, List<String> trainerList) {
        log.info("Updating trainer list for trainee: {}", username);
        Trainee trainee = traineeRepository.findByUsername(username).get();

        List<Trainer> trainers = trainerRepository.findByUsernames(trainerList);

        if (trainers.isEmpty()) {
            throw new IllegalArgumentException("Trainer list does not exist");
        }

        trainee.setTrainers(trainers);

        trainee = traineeRepository.save(trainee);
        log.info("Updated trainee {}'s trainer list successfully!", username);
        return trainers.stream().map(TrainerDtoMapper::toTrainerResponseInList).toList();
    }

    @TransactionalWithLogging
    public void deleteTrainee(String username) {
        log.info("User {} is attempting to delete account: {}", username);
        traineeRepository.deleteByUsername(username);
        log.info("Deleted trainer {}'s profile successfully", username);
    }


}

