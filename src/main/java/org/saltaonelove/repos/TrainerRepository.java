package org.saltaonelove.repos;

import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository {
    Trainer save(Trainer trainer);
    Optional<Trainer> findById(Long id);
    Optional<Trainer> findByUsername(String username);
    List<Trainer> findByUsernames(List<String> usernames);
    List<Training> findTrainerTrainingsByUsernameAndCriteria(String username, LocalDate from, LocalDate to, String traineeName, String trainingType);
    List<Trainer> findTrainersThatAreNotAssignedToTrainee(String traineeUsername);
    List<Trainer> findAll();
    void delete(Long id);
    Trainer getReferenceById(Long id);
}
