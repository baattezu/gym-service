package org.saltaonelove.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.saltaonelove.model.User;

import java.util.List;
import java.util.regex.Pattern;

class UserUtilTest {

    private UserUtil userUtil;
    private User user;

    @BeforeEach
    void setUp() {
        userUtil = new UserUtil();
        user = new User("John", "Doe");
    }

    @Test
    void testGenerateUsername_NoDuplicates() {
        List<User> existingUsers = List.of();

        String generatedUsername = userUtil.generateUsername(user, existingUsers);

        assertEquals("John.Doe", generatedUsername);
    }

    @Test
    void testGenerateUsername_WithDuplicates() {
        User existingUser = new User("John", "Doe");

        List<User> existingUsers = List.of(existingUser);

        String generatedUsername = userUtil.generateUsername(user, existingUsers);

        assertTrue(generatedUsername.matches("John.Doe\\d+"));
    }

    @RepeatedTest(5) // Генерируем пароль 5 раз, чтобы проверить случайность
    void testGenerateRandomPassword() {
        String password = userUtil.generateRandomPassword();

        assertNotNull(password);
        assertEquals(10, password.length());

        Pattern pattern = Pattern.compile("^[A-Za-z0-9]{10}$");
        assertTrue(pattern.matcher(password).matches());
    }
}