package org.saltaonelove.repos.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.saltaonelove.model.Training;
import org.saltaonelove.repos.TrainingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingRepositoryImpl implements TrainingRepository {

    private static final Logger log = LoggerFactory.getLogger(TrainingRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Training save(Training training) {
        try {
            if (training.getTrainingId() == null) {
                entityManager.persist(training);
            } else {
                entityManager.merge(training);
            }
            return training;
        } catch (Exception e) {
            log.error("Error while saving training: {}", e.getMessage());
            throw new RuntimeException("Error while saving training", e);
        }
    }

    @Override
    public List<Training> findAll() {
        return entityManager.createQuery("Select tg from Training tg", Training.class).getResultList();
    }

    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Training.class, id));
    }

    @Override
    public void delete(Long id) {
        Training training = entityManager.find(Training.class, id);
        if (training != null) {
            entityManager.remove(training);
        }
    }

    @Override
    public Training getReferenceById(Long id) {
        return entityManager.getReference(Training.class, id);
    }
}
