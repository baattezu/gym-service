package org.saltaonelove.repos;

import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository {
    Trainee save(Trainee trainee);
    Optional<Trainee> findById(Long id);
    Optional<Trainee> findByUsername(String username);
    List<Trainee> findAll();
    List<Training> findTraineeTrainingsByUsernameAndCriteria(String username, LocalDate from, LocalDate to, String trainerName, String trainingType);
    void delete(Long id);
    void deleteByUsername(String username);
    Trainee getReferenceById(Long id);
}
