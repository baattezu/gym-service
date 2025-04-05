package org.saltaonelove.service;

import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.trainer.TrainerRequest;
import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.dto.trainer.TrainerUpdateRequest;
import org.saltaonelove.dto.training.TrainingResponse;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.TrainingTypeRepository;
import org.saltaonelove.util.DtoMapper;
import org.saltaonelove.util.logging.LoggingUtil;
import org.saltaonelove.util.logging.annotation.TransactionalWithLogging;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainerService {

    private static final LoggingUtil log = LoggingUtil.getLogger(TrainerService.class);

    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private UserCredentialsService userUtil;

    public TrainerService(TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, UserCredentialsService userUtil) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.userUtil = userUtil;
    }

    @TransactionalWithLogging
    public Trainer registerTrainer(TrainerRequest trainerRequest) {
        log.info("Registering trainer: {} {}", trainerRequest.firstName(), trainerRequest.lastName());
        Trainer trainer = new Trainer(
                trainerRequest.firstName(), trainerRequest.lastName(),
                trainingTypeRepository.findByName(trainerRequest.specialization()).orElseThrow(
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

    @TransactionalWithLogging
    public Trainer toggleActivationOfAccount(String username) {
        Trainer trainer = trainerRepository.findByUsername(username).get();
        trainer.setActive(!trainer.isActive());
        trainer = trainerRepository.save(trainer);
        log.info("Trainer {} is {}", trainer.getUsername(), trainer.isActive() ? "activated" : "deactivated");
        return trainer;
    }


    public List<Trainer> listTrainers(String username) {
        return trainerRepository.findAll();
    }

    public TrainerResponse showProfile(String username) {
        log.info("Showing profile for user: {}", username);
        Trainer t = trainerRepository.findByUsername(username).get();
        return DtoMapper.toTrainerResponse(t);
    }

    @TransactionalWithLogging
    public TrainerResponse updateTrainer(TrainerUpdateRequest trainerRequest) {
        log.info("Updating trainer: {} {}", trainerRequest.firstName(), trainerRequest.lastName());
        Trainer t = trainerRepository.findByUsername(trainerRequest.username()).get();

        t.setFirstName(trainerRequest.firstName());
        t.setLastName(trainerRequest.lastName());
        t.setSpecialization(trainingTypeRepository.findByName(trainerRequest.specialization()).orElseThrow(() -> new IllegalArgumentException("Specialization not found")));
        t.setActive(trainerRequest.isActive());

        t = trainerRepository.save(t);
        log.info("Updated trainer {}'s profile successfully!", t.getUsername());
        return DtoMapper.toTrainerResponse(t);
    }

    @TransactionalWithLogging
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


    public List<TrainingResponse> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName, String trainingType) {
        log.info("Fetching trainings for trainer {}", username);
        return trainerRepository.findTrainerTrainingsByUsernameAndCriteria(
                username, fromDate, toDate, traineeName, trainingType)
                .stream().map(DtoMapper::toTrainingResponse).toList();
    }

}
