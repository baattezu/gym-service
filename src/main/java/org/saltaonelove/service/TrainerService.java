package org.saltaonelove.service;

import org.saltaonelove.dto.AuthRequest;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.TrainingTypeRepository;
import org.saltaonelove.util.UpdateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainerService {

    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private UserCredentialsService userUtil;

    public TrainerService(TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, UserCredentialsService userUtil) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.userUtil = userUtil;
    }

    @Transactional
    public Trainer registerTrainer(TrainerDTO trainerDTO) {
        log.info("Registering trainer: {} {}", trainerDTO.firstName(), trainerDTO.lastName());
        Trainer trainer = new Trainer(
                trainerDTO.firstName(), trainerDTO.lastName(),
                trainingTypeRepository.findByName(trainerDTO.specialization()).orElseThrow(
                        () -> new IllegalArgumentException("Specialization not found")
                )
        );
        trainer.setUsername(userUtil.generateUsername(trainer));
        trainer.setPassword(userUtil.generateRandomPassword());
        trainer = trainerRepository.save(trainer);
        log.info("Trainer {} successfully registered", trainer.getUsername());
        return trainer;
    }

    public Trainer loginForTrainer(AuthRequest auth) {
        return userUtil.authorize(auth,
                () -> trainerRepository.findByUsername(auth.username()).get());
    }

    @Transactional
    public Trainer toggleActivationOfAccount(AuthRequest auth) {
        loginForTrainer(auth);
        Trainer trainer = trainerRepository.findByUsername(auth.username()).get();
        trainer.setActive(!trainer.isActive());
        trainer = trainerRepository.save(trainer);
        log.info("Trainer {} is {}", trainer.getUsername(), trainer.isActive() ? "activated" : "deactivated");
        return trainer;
    }


    public List<Trainer> listTrainers(AuthRequest auth) {
        loginForTrainer(auth);
        return trainerRepository.findAll();
    }

    public Trainer showProfile(AuthRequest auth) {
        loginForTrainer(auth);
        log.info("Showing profile for user: {}", auth.username());
        return trainerRepository.findByUsername(auth.username()).get();
    }

    @Transactional
    public Trainer updateTrainer(AuthRequest auth, TrainerDTO trainerDto) {
        loginForTrainer(auth);
        log.info("Updating trainer: {} {}", trainerDto.firstName(), trainerDto.lastName());
        Trainer trainer = trainerRepository.findByUsername(auth.username()).get();

        UpdateUtil.setIfNotNull(trainerDto.firstName(), trainer::setFirstName);
        UpdateUtil.setIfNotNull(trainerDto.lastName(), trainer::setLastName);
        UpdateUtil.setIfNotNull(trainingTypeRepository.findByName(trainerDto.specialization())
                        .orElseThrow(() -> new IllegalArgumentException("Specialization not found"))
                , trainer::setSpecialization);

        trainer = trainerRepository.save(trainer);
        log.info("Updated trainer {}'s profile successfully!", auth.username());
        return trainer;
    }

    @Transactional
    public Trainer changePassword(AuthRequest auth, String newPassword) {
        loginForTrainer(auth);
        log.info("User {} is attempting to change their password", auth.username());
        Trainer trainer = trainerRepository.findByUsername(auth.username()).get();
        if (trainer.getPassword().equals(newPassword) || newPassword.length() < 10) {
            throw new IllegalArgumentException("New password repeats old password match or new password is too short");
        }
        trainer.setPassword(newPassword);

        trainer = trainerRepository.save(trainer);
        log.info("Trainer {} changed password successfully", auth.username());
        return trainer;
    }


    public List<Training> getTrainerTrainings(AuthRequest authRequest, LocalDate fromDate, LocalDate toDate, String traineeName, String trainingType) {
        loginForTrainer(authRequest);
        log.info("Fetching trainings for trainer {}", authRequest.username());
        return trainerRepository.findTrainerTrainingsByUsernameAndCriteria(authRequest.username(), fromDate, toDate, traineeName, trainingType);
    }

    public List<Trainer> getTrainersAvailableForTrainee(AuthRequest authRequest, String traineeName){
        loginForTrainer(authRequest);
        log.info("Fetching available trainers for trainee {}", traineeName);
        return trainerRepository.findTrainersThatAreNotAssignedToTrainee(traineeName);
    }

}
