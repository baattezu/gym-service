package org.saltaonelove.integration.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.InitModels;
import org.saltaonelove.config.JpaConfig;
import org.saltaonelove.model.entity.User;
import org.saltaonelove.repos.UserRepository;
import org.saltaonelove.repos.implementation.UserRepositoryImpl;
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
        UserRepositoryImpl.class,
        JpaConfig.class
})
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Import(UserRepositoryImpl.class)
@Transactional
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    public void setup() {
        user = InitModels.initTrainee();
    }

    @Test
    void testSaveAndFindAll() {
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername())));
    }

    @Test
    void testFindByUsername() {
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> result = userRepository.findByUsername(user.getUsername());
        assertTrue(result.isPresent());
    }

    @Test
    void testFindUsernamesByBase() {
        entityManager.persist(user);
        entityManager.flush();

        List<String> usernames = userRepository.findUsernamesByBase(user.getUsername().substring(0, 3));
        assertFalse(usernames.isEmpty());
        assertTrue(usernames.contains(user.getUsername()));
    }

    @Test
    void testFindUserPositionByUsername() {
        entityManager.persist(user);
        entityManager.flush();

        String position = userRepository.findUserPositionByUsername(user.getUsername());
        assertEquals("TRAINEE", position);
    }
}