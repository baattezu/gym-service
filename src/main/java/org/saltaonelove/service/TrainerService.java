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

    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private UserCredentialsService userUtil;

    @Transactional
    public Trainer registerTrainer(TrainerDTO trainerDTO) {
        Trainer trainer = new Trainer(
                trainerDTO.firstName(), trainerDTO.lastName(),
                trainingTypeRepository.findByName(trainerDTO.specialization()).orElseThrow(
                        () -> new IllegalArgumentException("Specialization not found")
                )
        );
        trainer.setUsername(userUtil.generateUsername(trainer));
        trainer.setPassword(userUtil.generateRandomPassword());
        return trainerRepository.save(trainer);
    }

    public Trainer loginForTrainer(AuthRequest auth) {
        Trainer trainer = trainerRepository.findByUsername(auth.username())
                .orElseThrow(() -> new IllegalArgumentException("Username not found: " + auth.username()));
        if (trainer.getPassword().equals(auth.password())) {
            log.info("Logged in user: " + auth.username());
            return trainer;
        }
        throw new IllegalArgumentException("Wrong password");
    }

    @Transactional
    public Trainer toggleActivationOfAccount(AuthRequest auth) {
        loginForTrainer(auth);
        Trainer trainer = trainerRepository.findByUsername(auth.username()).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found")
        );
        trainer.setActive(!trainer.isActive());
        log.info("Trainer is active: " + trainer.isActive());
        return trainerRepository.save(trainer);
    }


    public List<Trainer> listTrainers(AuthRequest auth) {
        loginForTrainer(auth);
        return trainerRepository.findAll();
    }

    public Trainer showProfile(AuthRequest auth) {
        loginForTrainer(auth);
        Trainer trainer = trainerRepository.findByUsername(auth.username())
                .orElseThrow(() -> new IllegalArgumentException("Username not found: " + auth.username()));
        return trainer;
    }

    @Transactional
    public Trainer updateTrainer(AuthRequest auth, TrainerDTO trainerDto) {
        loginForTrainer(auth);
        Trainer trainer = trainerRepository.findByUsername(auth.username()).orElseThrow(() -> new IllegalArgumentException("Trainer not found"));

        UpdateUtil.setIfNotNull(trainerDto.firstName(), trainer::setFirstName);
        UpdateUtil.setIfNotNull(trainerDto.lastName(), trainer::setLastName);
        UpdateUtil.setIfNotNull(trainingTypeRepository.findByName(trainerDto.specialization())
                        .orElseThrow(() -> new IllegalArgumentException("Specialization not found"))
                , trainer::setSpecialization);

        return trainerRepository.save(trainer);
    }

    @Transactional
    public Trainer changePassword(AuthRequest auth, String newPassword) {
        loginForTrainer(auth);
        Trainer trainer = trainerRepository.findByUsername(auth.username()).orElseThrow(()-> new IllegalArgumentException("Trainer not found: " + auth.username()));
        if (trainer.getPassword().equals(newPassword) || newPassword.length() < 10) {
            throw new IllegalArgumentException("New password repeats old password match or new password is too short");
        }
        trainer.setPassword(newPassword);
        return trainerRepository.save(trainer);
    }


    public List<Training> getTrainerTrainings(AuthRequest authRequest, LocalDate fromDate, LocalDate toDate, String traineeName, String trainingType) {
        loginForTrainer(authRequest);
        return trainerRepository.findTrainerTrainingsByUsernameAndCriteria(authRequest.username(), fromDate, toDate, traineeName, trainingType);
    }

    public List<Trainer> getTrainersAvailableForTrainee(AuthRequest authRequest, String traineeName){
        loginForTrainer(authRequest);
        return trainerRepository.findTrainersThatAreNotAssignedToTrainee(traineeName);
    }

}
