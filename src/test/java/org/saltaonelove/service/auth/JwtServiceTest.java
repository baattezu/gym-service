package org.saltaonelove.service.auth;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saltaonelove.gymshared.security.service.JwtService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

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
        UserDetails user = new org.springframework.security.core.userdetails.User(
                "johndoe",
                "somePassword",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));
        assertEquals("johndoe", jwtService.extractUsername(token));
    }

    @Test
    void testInvalidTokenThrowsException() {
        String invalidToken = "this.is.an.invalid.token";

        assertThrows(JwtException.class, () -> {jwtService.isValid(invalidToken);});
    }

}