package org.saltaonelove.repos;

import org.saltaonelove.model.Training;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository {
    Training save(Training training);
    List<Training> findAll();
    Optional<Training> findById(Long id);
    void delete(Long id);
    Training getReferenceById(Long id);
}
