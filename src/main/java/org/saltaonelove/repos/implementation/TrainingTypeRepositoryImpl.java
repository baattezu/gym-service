package org.saltaonelove.repos.implementation;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.saltaonelove.model.TrainingType;
import org.saltaonelove.repos.TrainingTypeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrainingType> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TrainingType.class, id));
    }

    @Override
    public Optional<TrainingType> findByName(String name) {
        return Optional.ofNullable(
                entityManager.createNamedQuery("TrainingType.findByName", TrainingType.class)
                .setParameter("ttName", name).getSingleResult());
    }

    @Override
    public List<TrainingType> findAll() {
        return entityManager.createQuery("Select tt from TrainingType tt", TrainingType.class).getResultList();
    }

    @Override
    public TrainingType getReferenceById(Long id) {
        return entityManager.getReference(TrainingType.class, id);
    }
}
