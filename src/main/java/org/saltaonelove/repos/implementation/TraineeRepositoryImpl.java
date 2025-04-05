package org.saltaonelove.repos.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.repos.TraineeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TraineeRepositoryImpl implements TraineeRepository {

    private static final Logger log = LoggerFactory.getLogger(TraineeRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Trainee save(Trainee trainee) {
        try {
            if (trainee.getUserId() == null) {
                entityManager.persist(trainee);
            } else {
                entityManager.merge(trainee);
            }
            return trainee;
        } catch (Exception e){
            log.error("Error while saving trainee: {}", e.getMessage());
            throw new RuntimeException("Error while saving trainee" ,e);
        }
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainee.class, id));
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        try {
            return Optional.of(entityManager
                    .createNamedQuery("Trainee.findByUsername", Trainee.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            throw new IllegalArgumentException("Could not find trainee with username: " + username);
        } catch (Exception e) {
            log.error("Unexpected error while fetching trainee: {}", e.getMessage(), e);
            throw e;
        }
    }


    @Override
    public List<Trainee> findAll() {
        return entityManager.createQuery("SELECT te FROM Trainee te", Trainee.class).getResultList();
    }

    @Override
    public List<Training> findTraineeTrainingsByUsernameAndCriteria(String username, LocalDate from, LocalDate to, String trainerName, String trainingType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> training = query.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        if (username != null && !username.isEmpty()) {
            Join<Training, Trainee> traineeJoin = training.join("trainee");
            predicates.add(cb.equal(traineeJoin.get("username"), username));
        }

        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("date"), from));
        }

        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("date"), to));
        }

        if (trainerName != null && !trainerName.isEmpty()) {
            Join<Training, Trainer> trainerJoin = training.join("trainer");
            predicates.add(cb.equal(trainerJoin.get("username"), trainerName));
        }

        if (trainingType != null && !trainingType.isEmpty()) {
            predicates.add(cb.equal(training.get("trainingType"), trainingType));
        }

        query.select(training).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

//    @Override
//    public List<Trainer> findTrainersThatAreNotAssignedToTrainee(String traineeUsername) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//
//        CriteriaQuery<Trainer> query = cb.createQuery(Trainer.class);
//        Root<Trainer> trainer = query.from(Trainer.class);
//
//        Subquery<Long> assignedTrainersSubquery = query.subquery(Long.class);
//        Root<Trainee> traineeRoot = assignedTrainersSubquery.from(Trainee.class);
//
//        Join<Trainee, Trainer> assignedTrainers = traineeRoot.join("trainers");
//
//        assignedTrainersSubquery.select(assignedTrainers.get("userId"))
//                .where(cb.equal(traineeRoot.get("username"), traineeUsername));
//
//        query.select(trainer)
//                .where(cb.not(trainer.get("userId").in(assignedTrainersSubquery)));
//
//        return entityManager.createQuery(query).getResultList();
//    }

    @Override
    public void delete(Long id) {
        Trainee trainee = entityManager.find(Trainee.class, id);
        if (trainee != null) {
            entityManager.remove(trainee);
        }
    }

    @Override
    public void deleteByUsername(String username) {
        Trainee trainee = findByUsername(username).orElse(null);
        if (trainee != null) {
            entityManager.remove(trainee);
        }
    }

    @Override
    public Trainee getReferenceById(Long id) {
        return entityManager.getReference(Trainee.class, id);
    }
}
