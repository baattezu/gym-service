package org.saltaonelove.integration.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.InitModels;
import org.saltaonelove.config.JpaConfig;
import org.saltaonelove.model.entity.Trainee;
import org.saltaonelove.model.entity.Trainer;
import org.saltaonelove.model.entity.Training;
import org.saltaonelove.model.entity.TrainingType;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.implementation.TrainerRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



@SpringBootTest(classes = {
        TrainerRepositoryImpl.class,
        JpaConfig.class
})
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Import(TrainerRepositoryImpl.class)
@Transactional
public class TrainerRepositoryIntegrationTest {

    @Autowired
    private TrainerRepository trainerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Trainer trainer;
    private Trainee trainee;
    private TrainingType trainingType;
    private Training training;

    @BeforeEach
    public void setup() {
        trainer = InitModels.initTrainer();
        trainee = InitModels.initTrainee();
        trainingType = InitModels.initTrainingType();
        training = InitModels.initTraining(trainee, trainer, trainingType);
    }

    @Test
    void testSaveAndFindById() {
        trainerRepository.save(trainer);

        Optional<Trainer> found = trainerRepository.findById(trainer.getUserId());
        assertTrue(found.isPresent());
        assertEquals(trainer.getUsername(), found.get().getUsername());
    }

    @Test
    void testFindByUsername() {
        entityManager.persist(trainingType);
        entityManager.persist(trainer);
        entityManager.flush();

        Optional<Trainer> result = trainerRepository.findByUsername(trainer.getUsername());
        assertTrue(result.isPresent());
    }

    @Test
    void testFindTrainerTrainingsByUsernameAndCriteria() {
        entityManager.persist(trainingType);
        entityManager.persist(trainer);
        entityManager.persist(trainee);
        entityManager.flush();

        entityManager.merge(training);
        entityManager.flush();

        List<Training> results = trainerRepository.findTrainerTrainingsByUsernameAndCriteria(
                trainer.getUsername(),
                training.getDate().minusMonths(1),
                training.getDate().plusMonths(1),
                trainee.getUsername(),
                trainingType.getName()
        );

        assertEquals(1, results.size());
        assertEquals(training.getDate(), results.get(0).getDate());
    }

    @Test
    void testFindByUsernames() {
        entityManager.persist(trainingType);
        entityManager.persist(trainer);
        entityManager.flush();

        List<Trainer> result = trainerRepository.findByUsernames(List.of(trainer.getUsername()));
        assertEquals(1, result.size());
        assertEquals(trainer.getUsername(), result.get(0).getUsername());
    }

    @Test
    void testDelete() {
        entityManager.persist(trainingType);
        entityManager.persist(trainer);
        entityManager.flush();

        trainerRepository.delete(trainer.getUserId());
        entityManager.flush();

        Optional<Trainer> result = trainerRepository.findById(trainer.getUserId());
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetReferenceById() {
        entityManager.persist(trainingType);
        entityManager.persist(trainer);
        entityManager.flush();

        Trainer reference = trainerRepository.getReferenceById(trainer.getUserId());
        assertEquals(trainer.getUserId(), reference.getUserId());
    }
}
