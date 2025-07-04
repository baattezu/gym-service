package org.saltaonelove.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.saltaonelove.clients.workload.WorkloadClient;
import org.saltaonelove.gymshared.model.workload.ActionType;
import org.saltaonelove.model.dto.training.TrainingRequest;
import org.saltaonelove.model.entity.Trainee;
import org.saltaonelove.model.entity.Trainer;
import org.saltaonelove.model.entity.Training;
import org.saltaonelove.model.entity.TrainingType;
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.TrainingRepository;
import org.saltaonelove.repos.TrainingTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class TrainingService {

    private TraineeRepository traineeRepository;
    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private TrainingRepository trainingRepository;
    private WorkloadClient workloadClient;

    public TrainingService(TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, TrainingRepository trainingRepository, WorkloadClient workloadClient) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingRepository = trainingRepository;
        this.workloadClient = workloadClient;
    }

    @Transactional
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

        workloadClient.updateTrainerWorkload(
                training.toWorkloadRequest(ActionType.ADD)
        );

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

    @Transactional
    public void cancelTraining(Long id) {
        log.info("Cancelling training {}", id);
        Training training = getTraining(id);
        trainingRepository.delete(id);

        workloadClient.updateTrainerWorkload(
                training.toWorkloadRequest(ActionType.DELETE)
        );
    }
}
