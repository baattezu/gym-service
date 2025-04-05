package org.saltaonelove.service;

import jakarta.persistence.EntityNotFoundException;
import org.saltaonelove.dto.training.TrainingDTO;
import org.saltaonelove.dto.training.TrainingRequest;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.TrainingRepository;
import org.saltaonelove.repos.TrainingTypeRepository;
import org.saltaonelove.util.logging.annotation.TransactionalWithLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TrainingService {

    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    private TraineeRepository traineeRepository;
    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private TrainingRepository trainingRepository;

    public TrainingService(TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, TrainingRepository trainingRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingRepository = trainingRepository;
    }

    @TransactionalWithLogging
    public Training createTraining(TrainingRequest trainingDTO) {
        log.info("Creating training {}", trainingDTO);
        Training training = new Training();

        Trainer trainer = trainerRepository.findByUsername(trainingDTO.trainerUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + trainingDTO.trainerUsername()));

        Trainee trainee = traineeRepository.findByUsername(trainingDTO.traineeUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + trainingDTO.traineeUsername()));

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingDTO.trainingName());
        training.setTrainingType(trainer.getSpecialization());
        training.setDate(trainingDTO.trainingDate());
        training.setDuration(trainingDTO.duration());

        training = trainingRepository.save(training);
        log.info("Created training {}", trainingDTO);
        return training;
    }

    public List<Training> listTrainings() {
        log.info("Listing trainings");
        return trainingRepository.findAll();
    }

    public Training getTraining(Long id) {
        log.info("Fetching training {}", id);
        return trainingRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Training with id " + id + " not found"));
    }

    public List<TrainingType> getTrainingTypes() {
        log.info("Fetching trainingTypes");
        return trainingTypeRepository.findAll();
    }

}
