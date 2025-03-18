package org.saltaonelove.repos;

import org.saltaonelove.model.TrainingType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingTypeRepository {
    Optional<TrainingType> findById(Long id);
    Optional<TrainingType> findByName(String name);
    List<TrainingType> findAll();
    TrainingType getReferenceById(Long id);
}
