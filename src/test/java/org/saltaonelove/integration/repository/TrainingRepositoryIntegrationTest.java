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
import org.saltaonelove.repos.TrainingRepository;
import org.saltaonelove.repos.implementation.TrainingRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {
        TrainingRepositoryImpl.class,
        JpaConfig.class
})
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Import(TrainingRepositoryImpl.class)
@Transactional
public class TrainingRepositoryIntegrationTest {

    @Autowired
    private TrainingRepository trainingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private Training training;

    @BeforeEach
    public void setup() {
        trainee = InitModels.initTrainee();
        trainer = InitModels.initTrainer();
        trainingType = InitModels.initTrainingType();
        training = InitModels.initTraining(trainee, trainer, trainingType);
    }

    @Test
    void testSaveAndFindById() {
        entityManager.persist(trainingType);
        entityManager.persist(trainee);
        entityManager.persist(trainer);
        entityManager.flush();

        trainingRepository.save(training);

        Optional<Training> found = trainingRepository.findById(training.getTrainingId());
        assertTrue(found.isPresent());
        assertEquals(training.getTrainingName(), found.get().getTrainingName());
    }

    @Test
    void testFindAll() {
        entityManager.persist(trainingType);
        entityManager.persist(trainee);
        entityManager.persist(trainer);
        entityManager.persist(training);
        entityManager.flush();

        List<Training> result = trainingRepository.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testDelete() {
        entityManager.persist(trainingType);
        entityManager.persist(trainee);
        entityManager.persist(trainer);
        entityManager.persist(training);
        entityManager.flush();

        trainingRepository.delete(training.getTrainingId());
        entityManager.flush();

        Optional<Training> result = trainingRepository.findById(training.getTrainingId());
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetReferenceById() {
        entityManager.persist(trainingType);
        entityManager.persist(trainee);
        entityManager.persist(trainer);
        entityManager.persist(training);
        entityManager.flush();

        Training ref = trainingRepository.getReferenceById(training.getTrainingId());
        assertEquals(training.getTrainingId(), ref.getTrainingId());
    }
}