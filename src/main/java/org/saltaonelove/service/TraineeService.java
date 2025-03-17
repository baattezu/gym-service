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
        Trainee trainee = new Trainee(
                traineeDTO.firstName(), traineeDTO.lastName(),
                traineeDTO.dateOfBirth() != null ? LocalDate.parse(traineeDTO.dateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null,
                traineeDTO.address()
        );
        trainee.setUsername(userUtil.generateUsername(trainee));
        trainee.setPassword(userUtil.generateRandomPassword());
        return traineeRepository.save(trainee);
    }

    public Trainee loginForTrainee(AuthRequest auth) {
        Trainee trainee = traineeRepository.findByUsername(auth.username())
                .orElseThrow(() -> new IllegalArgumentException("Username not found: " + auth.username()));
        if (trainee.getPassword().equals(auth.password())) {
            log.info("Logged in user: " + auth.username());
            return trainee;
        }
        throw new IllegalArgumentException("Wrong password");
    }

    @Transactional
    public Trainee toggleActivationOfAccount(AuthRequest auth) {
        loginForTrainee(auth);
        Trainee trainee = traineeRepository.findByUsername(auth.username()).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found")
        );
        trainee.setActive(!trainee.isActive());
        log.info("Trainee is active: " + trainee.isActive());
        return traineeRepository.save(trainee);
    }

    public List<Trainee> listTrainees(AuthRequest auth) {
        loginForTrainee(auth);
        return traineeRepository.findAll();
    }

    public Trainee showProfile(AuthRequest auth) {
        loginForTrainee(auth);
        Trainee trainee = traineeRepository.findByUsername(auth.username())
                .orElseThrow(() -> new IllegalArgumentException("Username not found: " + auth.username()));
        return trainee;
    }

    @Transactional
    public Trainee updateTrainee(AuthRequest auth, TraineeDTO traineeDto) {
        loginForTrainee(auth);
        Trainee trainee = traineeRepository.findByUsername(auth.username()).orElseThrow(() -> new IllegalArgumentException("Trainee not found"));

        UpdateUtil.setIfNotNull(traineeDto.firstName(), trainee::setFirstName);
        UpdateUtil.setIfNotNull(traineeDto.lastName(), trainee::setLastName);
        UpdateUtil.setIfNotNull(traineeDto.address(), trainee::setAddress);
        UpdateUtil.setIfNotNull(traineeDto.dateOfBirth(), trainee::setDateOfBirth);

        log.info("Updating trainee: " + trainee.getUsername());
        return traineeRepository.save(trainee);
    }

    public List<Training> getTraineeTrainings(AuthRequest authRequest, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        loginForTrainee(authRequest);
        return traineeRepository.findTraineeTrainingsByUsernameAndCriteria(authRequest.username(), fromDate, toDate, trainerName, trainingType);
    }

    @Transactional
    public Trainee changePassword(AuthRequest auth, String newPassword) {
        loginForTrainee(auth);
        Trainee trainee = traineeRepository.findByUsername(auth.username()).orElseThrow(()-> new IllegalArgumentException("Trainer not found: " + auth.username()));
        if (trainee.getPassword().equals(newPassword) || newPassword.length() < 10) {
            throw new IllegalArgumentException("New password repeats old password match or new password is too short");
        }
        trainee.setPassword(newPassword);
        return traineeRepository.save(trainee);
    }

    @Transactional
    public Trainee updateTrainerList(AuthRequest auth, List<Trainer> trainerList) {
        loginForTrainee(auth);
        Trainee trainee = traineeRepository.findByUsername(auth.username()).orElseThrow(()-> new IllegalArgumentException("Trainer not found: " + auth.username()));
        trainee.setTrainers(trainerList);
        return traineeRepository.save(trainee);
    }

    @Transactional
    public void deleteTrainee(String username) {
        traineeRepository.deleteByUsername(username);
    }


}

