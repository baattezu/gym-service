package org.saltaonelove.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.dto.AuthRequest;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.User;
import org.saltaonelove.repos.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCredentialsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCredentialsService userCredentialsService;

    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainee = InitModels.initTrainee();
        trainer = InitModels.initTrainer();
    }

    @Test
    void generateUsername_noConflicts_returnsBaseUsername() {
        User user = new User("John", "Doe");
        when(userRepository.findAll()).thenReturn(List.of());

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
    void testAuthorize() {
        AuthRequest authRequest = new AuthRequest(trainee.getUsername(), trainee.getPassword());
        Supplier<Trainee> identityProvider = () -> trainee;

        trainee = userCredentialsService.authorize(authRequest, identityProvider);

        assertEquals(authRequest.username(), trainee.getUsername());
    }

    @Test
    void testAuthorizeWrongPassword() {
        AuthRequest authRequest = new AuthRequest(trainee.getUsername(), "WrongPassword");
        Supplier<Trainee> identityProvider = () -> trainee;

        assertThrows(IllegalArgumentException.class, () -> trainee = userCredentialsService.authorize(authRequest, identityProvider));
    }

}
