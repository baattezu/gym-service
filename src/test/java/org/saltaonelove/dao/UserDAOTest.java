package org.saltaonelove.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.User;
import org.saltaonelove.util.UserUtil;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDAOTest {

    @Mock
    private Storage storage;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private UserUtil userUtil;

    @InjectMocks
    private UserDAO userDAO;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "Doe");
        user.setUserId(1L);
        user.setPassword("securePass123");
    }

    @Test
    void testSave_NewUser() {
        when(idGenerator.nextId("user")).thenReturn(100L);
        when(storage.findAll("user")).thenReturn(List.of());
        when(userUtil.generateUsername(any(User.class), anyList())).thenReturn("John.Doe");
        when(userUtil.generateRandomPassword()).thenReturn("randomPass123");

        User newUser = new User("Jane", "Doe");
        User savedUser = userDAO.save(newUser);

        assertNotNull(savedUser.getUserId());
        assertEquals(100L, savedUser.getUserId());
        assertEquals("John.Doe", savedUser.getUsername());
        assertEquals("randomPass123", savedUser.getPassword());

        verify(idGenerator).nextId("user");
        verify(userUtil).generateUsername(any(User.class), anyList());
        verify(userUtil).generateRandomPassword();
        verify(storage).save("user", 100L, newUser);
    }

    @Test
    void testSave_ExistingUser() {
        userDAO.save(user);

        verify(storage).save("user", 1L, user);
        verify(idGenerator, never()).nextId(anyString());
        verify(userUtil, never()).generateUsername(any(User.class), anyList());
        verify(userUtil, never()).generateRandomPassword();
    }

    @Test
    void testUpdate() {
        user.setUsername("newUsername");
        user.setPassword("newPassword");

        User updatedUser = userDAO.update(user);

        assertEquals("newUsername", updatedUser.getUsername());
        assertEquals("newPassword", updatedUser.getPassword());

        verify(storage).save("user", 1L, user);
    }

    @Test
    void testFindById() {
        when(storage.findById("user", 1L)).thenReturn(user);

        User foundUser = userDAO.findById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getUserId());
        assertEquals("John.Doe", foundUser.getUsername());

        verify(storage).findById("user", 1L);
    }

    @Test
    void testDelete() {
        userDAO.delete(1L);

        verify(storage).delete("user", 1L);
    }

    @Test
    void testFindAll() {
        when(storage.findAll("user")).thenReturn(List.of(user));

        List<User> users = userDAO.findAll();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));

        verify(storage).findAll("user");
    }
}