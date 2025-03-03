package org.saltaonelove.dao.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    private Storage storage;
    private static final String NAMESPACE = "testNamespace";

    @BeforeEach
    void setUp() {
        storage = new Storage();
    }

    @Test
    void testSaveAndFindById() {
        User user = new User("John", "Doe");
        user.setUserId(1L);

        storage.save(NAMESPACE, 1L, user);
        User foundUser = storage.findById(NAMESPACE, 1L);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void testFindById_NotFound() {
        User result = storage.findById(NAMESPACE, 99L);
        assertNull(result);
    }

    @Test
    void testDelete() {
        User user = new User("John", "Doe");
        user.setUserId(1L);

        storage.save(NAMESPACE, 1L, user);
        storage.delete(NAMESPACE, 1L);

        User foundUser = storage.findById(NAMESPACE, 1L);
        assertNull(foundUser);
    }

    @Test
    void testDelete_NonExistent() {
        assertDoesNotThrow(() -> storage.delete(NAMESPACE, 99L));
    }

    @Test
    void testFindAll() {
        User user1 = new User("Alice", "Smith");
        user1.setUserId(1L);
        User user2 = new User("Bob", "Johnson");
        user2.setUserId(2L);

        storage.save(NAMESPACE, 1L, user1);
        storage.save(NAMESPACE, 2L, user2);

        List<User> users = storage.findAll(NAMESPACE);

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testFindAll_EmptyNamespace() {
        List<User> users = storage.findAll(NAMESPACE);
        assertTrue(users.isEmpty());
    }
}