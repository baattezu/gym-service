package org.saltaonelove.repos.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.repos.TrainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainerRepositoryImpl implements TrainerRepository {

    private static final Logger log = LoggerFactory.getLogger(TrainerRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Trainer save(Trainer trainer) {
        try {
            if (trainer.getUserId() == null) {
                entityManager.persist(trainer);
            } else {
                entityManager.merge(trainer);
            }
            return trainer;
        } catch (Exception e) {
            log.error("Error while saving trainee: {}", e.getMessage());
            throw new RuntimeException("Error while saving trainee", e);
        }
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainer.class, id));
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        try {
            return Optional.of(entityManager
                    .createNamedQuery("Trainer.findByUsername", Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            throw new IllegalArgumentException("Could not find trainer with username: " + username);
        } catch (Exception e) {
            log.error("Unexpected error while fetching trainer: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Trainer> findByUsernames(List<String> usernames) {
        return entityManager.createNamedQuery("Trainer.findByUsernames", Trainer.class).setParameter("usernames", usernames).getResultList();
    }

    @Override
    public List<Training> findTrainerTrainingsByUsernameAndCriteria(String username, LocalDate from, LocalDate to, String traineeName, String trainingType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> training = query.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        if (username != null && !username.isEmpty()) {
            Join<Training, Trainer> trainerJoin = training.join("trainer");
            predicates.add(cb.equal(trainerJoin.get("username"), username));
        }

        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("date"), from));
        }

        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("date"), to));
        }

        if (traineeName != null && !traineeName.isEmpty()) {
            Join<Training, Trainee> traineeJoin = training.join("trainee");
            predicates.add(cb.equal(traineeJoin.get("username"), traineeName));
        }

        if (trainingType != null && !trainingType.isEmpty()) {
            predicates.add(cb.equal(training.get("trainingType"), trainingType));
        }

        query.select(training).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Trainer> findTrainersThatAreNotAssignedToTrainee(String traineeUsername) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Trainer> query = cb.createQuery(Trainer.class);
        Root<Trainer> trainer = query.from(Trainer.class);

        Subquery<Long> assignedTrainersSubquery = query.subquery(Long.class);
        Root<Trainee> traineeRoot = assignedTrainersSubquery.from(Trainee.class);

        Join<Trainee, Trainer> assignedTrainers = traineeRoot.join("trainers");

        assignedTrainersSubquery.select(assignedTrainers.get("trainer_id"))
                .where(cb.equal(traineeRoot.get("username"), traineeUsername));

        query.select(trainer)
                .where(cb.not(trainer.get("id").in(assignedTrainersSubquery)));

        return entityManager.createQuery(query).getResultList();
    }


    @Override
    public List<Trainer> findAll() {
        return entityManager.createQuery("Select tr from Trainer tr", Trainer.class).getResultList();
    }

    @Override
    public void delete(Long id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer != null) {
            entityManager.remove(trainer);
        }
    }

    @Override
    public Trainer getReferenceById(Long id) {
        return entityManager.getReference(Trainer.class, id);
    }
}
