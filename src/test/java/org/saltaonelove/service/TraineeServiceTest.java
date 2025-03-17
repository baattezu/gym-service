package org.saltaonelove.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.dto.AuthRequest;
import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.repos.TraineeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    private static final Logger log = LoggerFactory.getLogger(TraineeServiceTest.class);
    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserCredentialsService userUtil;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;
    private AuthRequest traineeAuth;

    @BeforeEach
    void setUp() {
        trainee = InitModels.initTrainee();
        traineeAuth = new AuthRequest(trainee.getUsername(), trainee.getPassword());
    }

    @Test
    void testRegisterTrainee() {
        when(userUtil.generateUsername(any(Trainee.class))).thenReturn("John.Doe");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.registerTrainee(new TraineeDTO("John", "Doe"));

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testRegisterTraineeWithDetails() {
        when(userUtil.generateUsername(any(Trainee.class))).thenReturn("John.Doe");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.registerTrainee(
                new TraineeDTO("John", "Doe",
                        "2001-01-01", "address1")
        );

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("address1", result.getAddress());
        assertEquals(LocalDate.of(2001, 1, 1), result.getDateOfBirth());

        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testLogin() {
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));

        traineeService.loginForTrainee(traineeAuth);

        verify(traineeRepository).findByUsername(traineeAuth.username());
    }

    @Test
    public void testToggleActivationOfAccount() {
        boolean traineeActiveBefore = trainee.isActive();

        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee newTrainee = traineeService.toggleActivationOfAccount(traineeAuth);

        assertNotNull(newTrainee);

        assertEquals(!traineeActiveBefore, newTrainee.isActive());

        verify(traineeRepository, times(2)).findByUsername(traineeAuth.username());

        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testLoginWrongPassword() {
        AuthRequest wrongPasswordAuth = new AuthRequest(traineeAuth.username(), "1337S1mple");

        when(traineeRepository.findByUsername(wrongPasswordAuth.username())).thenReturn(Optional.ofNullable(trainee));

        assertThrows(IllegalArgumentException.class, () -> traineeService.loginForTrainee(wrongPasswordAuth));

        verify(traineeRepository).findByUsername(traineeAuth.username());
    }

    @Test
    void testListTrainees() {
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.of(trainee));

        List<Trainee> result = traineeService.listTrainees(traineeAuth);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(traineeRepository).findAll();
    }

    @Test
    void testShowTraineeProfile() {
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.showProfile(traineeAuth);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());

        verify(traineeRepository, times(2)).findByUsername(traineeAuth.username());
    }

    @Test
    void testUpdateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "NewLastName", "2001-01-01", "New Address");

        Trainee updTrainee = trainee;
        updTrainee.setLastName(traineeDTO.lastName());
        updTrainee.setAddress(traineeDTO.address());

        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee result = traineeService.updateTrainee(traineeAuth, traineeDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("NewLastName", result.getLastName());
        assertEquals("New Address", result.getAddress());
        assertEquals(LocalDate.of(2001, 1, 1), result.getDateOfBirth());

        verify(traineeRepository, times(2)).findByUsername(traineeAuth.username());
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    public void testChangePassword(){
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        String newPassword = "newPassword123";

        Trainee changedPassword = traineeService.changePassword(traineeAuth, newPassword);

        assertNotNull(changedPassword);
        assertEquals(newPassword, changedPassword.getPassword());

        verify(traineeRepository, times(2)).findByUsername(traineeAuth.username());
    }

    @Test
    public void testChangePasswordWrongPasswordLength(){
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));

        String newPasswordThatShort = "newPa";

        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword(traineeAuth, newPasswordThatShort));

        verify(traineeRepository, times(2)).findByUsername(traineeAuth.username());
        verify(traineeRepository, never()).save(any(Trainee.class));
    }

    @Test
    public void testChangePasswordRepeatsOldPassword(){
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));

        String newPasswordThatRepeatsOld = "password1";

        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword(traineeAuth, newPasswordThatRepeatsOld));

        verify(traineeRepository, times(2)).findByUsername(traineeAuth.username());
        verify(traineeRepository, never()).save(any(Trainee.class));
    }

    @Test
    void testUpdateTrainerList(){
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        List<Trainer> newTrainerList = List.of(InitModels.initTrainer());
        traineeService.updateTrainerList(traineeAuth, newTrainerList);

        assertNotNull(trainee.getTrainers());
        assertEquals(1, trainee.getTrainers().size());
        assertEquals(InitModels.initTrainer().getFirstName(), trainee.getTrainers().get(0).getFirstName());

        verify(traineeRepository, times(2)).findByUsername(traineeAuth.username());
        verify(traineeRepository).save(any(Trainee.class));
    }
}