package org.saltaonelove.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.model.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        String testSecret = "bXktdmVyeS1zZWNyZXQtc2VjcmV0LWZvci10ZXN0aW5n"; // base64("my-very-secret-secret-for-testing")
        String expireTime = "3600000";

        ReflectionTestUtils.setField(jwtService, "secretKey", testSecret);
        ReflectionTestUtils.setField(jwtService, "expireTime", expireTime);
    }

    @Test
    void testGenerateAndValidateToken() {
        User user = new User();
        user.setUsername("johndoe");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));
        assertEquals("johndoe", jwtService.extractUsername(token));
    }

    @Test
    void testInvalidTokenThrowsException() {
        String invalidToken = "this.is.an.invalid.token";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtService.isValid(invalidToken);
        });

        assertTrue(exception.getMessage().contains("Invalid JWT"));
    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        ReflectionTestUtils.setField(jwtService, "expireTime", "1"); // 1ms

        User user = new User();
        user.setUsername("expiredUser");

        String token = jwtService.generateToken(user);

        Thread.sleep(5);

        assertThrows(BadCredentialsException.class, () -> jwtService.isValid(token));
    }
}