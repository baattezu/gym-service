package org.saltaonelove.service;

import org.saltaonelove.dto.TrainingDTO;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.TrainingRepository;
import org.saltaonelove.repos.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TrainingService {

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
        Training training = new Training();

        Trainer trainer = trainerRepository.getReferenceById(trainingDTO.trainerId());
        Trainee trainee = traineeRepository.getReferenceById(trainingDTO.traineeId());
        TrainingType trainingType = trainingTypeRepository.getReferenceById(trainingDTO.trainingTypeId());

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingDTO.trainingName());
        training.setTrainingType(trainingType);
        training.setDate(trainingDTO.date());
        training.setDuration(trainingDTO.duration());
        return trainingRepository.save(training);
    }

    public List<Training> listTrainings() {
        return trainingRepository.findAll();
    }

    public Training getTraining(Long id) {
        return trainingRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Training with id " + id + " not found"));
    }

    public List<TrainingType> getTrainingTypes() {
        return trainingTypeRepository.findAll();
    }

}
