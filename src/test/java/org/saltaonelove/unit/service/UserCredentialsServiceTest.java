package org.saltaonelove.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.metrics.AuthMetrics;
import org.saltaonelove.model.dto.auth.AuthRequest;
import org.saltaonelove.model.dto.auth.ChangeLoginRequest;
import org.saltaonelove.model.entity.Trainee;
import org.saltaonelove.model.entity.Trainer;
import org.saltaonelove.model.entity.User;
import org.saltaonelove.repos.UserRepository;
import org.saltaonelove.service.UserCredentialsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCredentialsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

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
        when(encoder.matches(any(), any())).thenReturn(true);
        when(encoder.encode(any())).thenReturn("NewPassword");
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
        when(encoder.matches(any(), any())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userCredentialsService.changeLogin(authRequest.username(), changeLoginRequest));
    }

}
