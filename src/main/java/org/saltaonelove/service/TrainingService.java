package org.saltaonelove.service;

import jakarta.persistence.EntityNotFoundException;
import org.saltaonelove.dto.TrainingDTO;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.TrainingRepository;
import org.saltaonelove.repos.TrainingTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TrainingService {

    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Transactional
    public Training createTraining(TrainingDTO trainingDTO) {
        log.info("Creating training {}", trainingDTO);
        Training training = new Training();

        Trainer trainer = trainerRepository.findById(trainingDTO.trainerId())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with ID: " + trainingDTO.trainerId()));

        Trainee trainee = traineeRepository.findById(trainingDTO.traineeId())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with ID: " + trainingDTO.traineeId()));

        TrainingType trainingType = trainingTypeRepository.findById(trainingDTO.trainingTypeId())
                .orElseThrow(() -> new EntityNotFoundException("TrainingType not found with ID: " + trainingDTO.trainingTypeId()));

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingDTO.trainingName());
        training.setTrainingType(trainingType);
        training.setDate(trainingDTO.date());
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
