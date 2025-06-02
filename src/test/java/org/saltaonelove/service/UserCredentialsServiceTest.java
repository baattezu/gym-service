package org.saltaonelove.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.auth.ChangeLoginRequest;
import org.saltaonelove.exception.exceptions.AuthException;
import org.saltaonelove.metrics.AuthMetrics;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.User;
import org.saltaonelove.repos.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCredentialsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthMetrics authMetrics;

    @InjectMocks
    private UserCredentialsService userCredentialsService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = InitModels.initTrainee();
    }

    @Test
    void generateUsername_noConflicts_returnsBaseUsername() {
        User user = new User("John", "Doe");

        String username = userCredentialsService.generateUsername(user);

        assertEquals("John.Doe", username);
    }

    @Test
    void generateUsername_withExistingUsers_generatesUniqueUsername() {
        User user = new User("Jane", "Smith");
        String baseUsername = user.getFirstName() + "." + user.getLastName();

        Trainee existingUser1 = new Trainee("Jane", "Smith");
        existingUser1.setUsername("Jane.Smith");
        Trainer existingUser2 = new Trainer("Jane", "Smith");
        existingUser2.setUsername("Jane.Smith1");

        when(userRepository.findUsernamesByBase(baseUsername)).thenReturn(
                List.of(existingUser1.getUsername(), existingUser2.getUsername()));

        String username = userCredentialsService.generateUsername(user);

        assertEquals("Jane.Smith2", username);
    }

    @Test
    void generateRandomPassword_returnsValidPassword() {
        String password = userCredentialsService.generateRandomPassword();

        assertNotNull(password);
        assertEquals(10, password.length());
        assertTrue(password.matches("[A-Za-z0-9]{10}"));
    }

    @Test
    void testChangePasswordInUserCredentialsService() {
        AuthRequest authRequest = new AuthRequest(trainee.getUsername(), trainee.getPassword());
        ChangeLoginRequest changeLoginRequest = new ChangeLoginRequest(authRequest.username(), authRequest.password(), "NewPassword");

        when(userRepository.findByUsername(trainee.getUsername())).thenReturn(Optional.of(trainee));
        when(userRepository.save(trainee)).thenReturn(trainee);

        User user = userCredentialsService.changeLogin(authRequest.username(), changeLoginRequest);

        assertNotNull(user);
        assertEquals("NewPassword", user.getPassword());

        verify(userRepository).save(trainee);
    }

    @Test
    void testChangePasswordInUserCredentialsServiceOldPasswordIsWrong() {
        AuthRequest authRequest = new AuthRequest(trainee.getUsername(), trainee.getPassword());
        ChangeLoginRequest changeLoginRequest = new ChangeLoginRequest(authRequest.username(), "WrongOldPassword", "NewPassword");

        when(userRepository.findByUsername(trainee.getUsername())).thenReturn(Optional.of(trainee));

        assertThrows(IllegalArgumentException.class, () -> userCredentialsService.changeLogin(authRequest.username(), changeLoginRequest));
    }

}
