package org.saltaonelove.integration.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.InitModels;
import org.saltaonelove.config.JpaConfig;
import org.saltaonelove.model.entity.TrainingType;
import org.saltaonelove.repos.TrainingTypeRepository;
import org.saltaonelove.repos.implementation.TrainingTypeRepositoryImpl;
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
        TrainingTypeRepositoryImpl.class,
        JpaConfig.class
})
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Import(TrainingTypeRepositoryImpl.class)
@Transactional
public class TrainingTypeRepositoryIntegrationTest {

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private TrainingType trainingType;

    @BeforeEach
    public void setup() {
        trainingType = InitModels.initTrainingType();
    }

    @Test
    void testFindById() {
        entityManager.persist(trainingType);
        entityManager.flush();

        Optional<TrainingType> found = trainingTypeRepository.findById(trainingType.getTrainingTypeId());
        assertTrue(found.isPresent());
        assertEquals(trainingType.getName(), found.get().getName());
    }

    @Test
    void testFindByName() {
        entityManager.persist(trainingType);
        entityManager.flush();

        Optional<TrainingType> result = trainingTypeRepository.findByName(trainingType.getName());
        assertTrue(result.isPresent());
        assertEquals(trainingType.getTrainingTypeId(), result.get().getTrainingTypeId());
    }

    @Test
    void testFindAll() {
        entityManager.persist(trainingType);
        entityManager.flush();

        List<TrainingType> list = trainingTypeRepository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void testGetReferenceById() {
        entityManager.persist(trainingType);
        entityManager.flush();
        entityManager.clear(); // simulate detached state

        TrainingType ref = trainingTypeRepository.getReferenceById(trainingType.getTrainingTypeId());
        assertEquals(trainingType.getTrainingTypeId(), ref.getTrainingTypeId());
    }
}