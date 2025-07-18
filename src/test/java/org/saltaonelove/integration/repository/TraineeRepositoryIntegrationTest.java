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
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.implementation.TraineeRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


//@DataJpaTest
@SpringBootTest(classes = {
        TraineeRepositoryImpl.class,
        JpaConfig.class
})
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties") // опционально
@Import(TraineeRepositoryImpl.class)
@Transactional
public class TraineeRepositoryIntegrationTest {

    @Autowired
    private TraineeRepository traineeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private Training training;

    @BeforeEach
    public void setup() {
        trainer = InitModels.initTrainer();
        trainee = InitModels.initTrainee();
        trainingType = InitModels.initTrainingType();
        training = InitModels.initTraining(
                trainee, trainer, trainingType
        );
    }

    @Test
    void testSaveAndFindById() {
        traineeRepository.save(trainee);

        Optional<Trainee> found = traineeRepository.findById(trainee.getUserId());
        assertTrue(found.isPresent());
        assertEquals(trainee.getUsername(), found.get().getUsername());
    }

    @Test
    void testFindByUsername() {
        entityManager.persist(trainee);

        Optional<Trainee> result = traineeRepository.findByUsername(trainee.getUsername());
        assertTrue(result.isPresent());
    }

    @Test
    void testFindTraineeTrainingsByUsernameAndCriteria() {
        entityManager.persist(trainingType);
        entityManager.persist(trainer);
        entityManager.persist(trainee);
        entityManager.flush();
        entityManager.merge(training);
        entityManager.flush();

        List<Training> results = traineeRepository.findTraineeTrainingsByUsernameAndCriteria(
                trainee.getUsername(),
                training.getDate().minusMonths(1),
                training.getDate().plusMonths(1),
                trainer.getUsername(),
                training.getTrainingType().getName()
        );

        assertEquals(1, results.size());
        assertEquals(training.getDate(), results.get(0).getDate());
    }

    @Test
    void testDeleteByUsername() {
        entityManager.persist(trainee);
        entityManager.flush();

        traineeRepository.deleteByUsername(trainee.getUsername());
        entityManager.flush();

        assertThrows(IllegalArgumentException.class, () -> traineeRepository.findByUsername(trainee.getUsername()).isEmpty());
    }
}