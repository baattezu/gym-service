package org.saltaonelove.service;

import org.saltaonelove.dto.AuthRequest;
import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.util.UpdateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
public class TraineeService {

    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private UserCredentialsService userUtil;

    @Transactional
    public Trainee registerTrainee(TraineeDTO traineeDTO) {
        log.info("Registering trainee: {} {}", traineeDTO.firstName(), traineeDTO.lastName());
        Trainee trainee = new Trainee(
                traineeDTO.firstName(), traineeDTO.lastName(),
                traineeDTO.dateOfBirth() != null ? LocalDate.parse(traineeDTO.dateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null,
                traineeDTO.address()
        );
        trainee.setUsername(userUtil.generateUsername(trainee));
        trainee.setPassword(userUtil.generateRandomPassword());
        trainee = traineeRepository.save(trainee);
        log.info("Trainer {} successfully registered", trainee.getUsername());
        return trainee;
    }

    public Trainee loginForTrainee(AuthRequest auth) {
        log.info("Trainee login attempt: {}", auth.username());
        Trainee trainee = traineeRepository.findByUsername(auth.username())
                .orElseThrow(() -> new IllegalArgumentException("Username not found: " + auth.username()));
        if (trainee.getPassword().equals(auth.password())) {
            log.info("Logged in user: " + auth.username());
            return trainee;
        }
        log.warn("Login attempt failed for user: {}", auth.username());
        throw new IllegalArgumentException("Wrong password");
    }

    @Transactional
    public Trainee toggleActivationOfAccount(AuthRequest auth) {
        loginForTrainee(auth);
        Trainee trainee = traineeRepository.findByUsername(auth.username()).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found")
        );
        trainee.setActive(!trainee.isActive());
        log.info("Trainee {} is {}", trainee.getUsername(), trainee.isActive() ? "activated" : "deactivated");
        return traineeRepository.save(trainee);
    }

    public List<Trainee> listTrainees(AuthRequest auth) {
        loginForTrainee(auth);
        return traineeRepository.findAll();
    }

    public Trainee showProfile(AuthRequest auth) {
        loginForTrainee(auth);
        log.info("Showing profile for user: {}", auth.username());
        Trainee trainee = traineeRepository.findByUsername(auth.username())
                .orElseThrow(() -> new IllegalArgumentException("Username not found: " + auth.username()));
        return trainee;
    }

    @Transactional
    public Trainee updateTrainee(AuthRequest auth, TraineeDTO traineeDto) {
        loginForTrainee(auth);
        log.info("Updating trainee: {} {}", traineeDto.firstName(), traineeDto.lastName());
        Trainee trainee = traineeRepository.findByUsername(auth.username()).orElseThrow(() -> new IllegalArgumentException("Trainee not found"));

        UpdateUtil.setIfNotNull(traineeDto.firstName(), trainee::setFirstName);
        UpdateUtil.setIfNotNull(traineeDto.lastName(), trainee::setLastName);
        UpdateUtil.setIfNotNull(traineeDto.address(), trainee::setAddress);
        UpdateUtil.setIfNotNull(traineeDto.dateOfBirth(), trainee::setDateOfBirth);

        trainee = traineeRepository.save(trainee);
        log.info("Updated trainee {}'s profile successfully!", auth.username());
        return trainee;
    }

    public List<Training> getTraineeTrainings(AuthRequest authRequest, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        loginForTrainee(authRequest);
        return traineeRepository.findTraineeTrainingsByUsernameAndCriteria(authRequest.username(), fromDate, toDate, trainerName, trainingType);
    }

    @Transactional
    public Trainee changePassword(AuthRequest auth, String newPassword) {
        loginForTrainee(auth);
        log.info("User {} is attempting to change their password", auth.username());
        Trainee trainee = traineeRepository.findByUsername(auth.username())
                .orElseThrow(()-> new IllegalArgumentException("Trainer not found: " + auth.username()));
        if (trainee.getPassword().equals(newPassword) || newPassword.length() < 10) {
            throw new IllegalArgumentException("New password repeats old password match or new password is too short");
        }
        trainee.setPassword(newPassword);

        trainee = traineeRepository.save(trainee);
        log.info("Trainee {} changed password successfully", auth.username());
        return trainee;
    }

    @Transactional
    public Trainee updateTrainerList(AuthRequest auth, List<Trainer> trainerList) {
        loginForTrainee(auth);
        log.info("Updating trainer list for trainee: {}", auth.username());
        Trainee trainee = traineeRepository.findByUsername(auth.username())
                .orElseThrow(()-> new IllegalArgumentException("Trainer not found: " + auth.username()));
        trainee.setTrainers(trainerList);

        trainee = traineeRepository.save(trainee);
        log.info("Updated trainee {}'s trainer list successfully!", auth.username());
        return trainee;
    }

    @Transactional
    public void deleteTrainee(AuthRequest auth, String username) {
        loginForTrainee(auth);
        log.info("User {} is attempting to delete account: {}", auth, username);
        traineeRepository.deleteByUsername(username);
        log.info("Deleted trainer {}'s profile successfully", username);
    }


}

