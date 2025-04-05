package org.saltaonelove.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.trainee.TraineeRegisterRequest;
import org.saltaonelove.dto.trainee.TraineeResponse;
import org.saltaonelove.dto.trainee.TraineeUpdateRequest;
import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.TrainerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

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

        Trainee result = traineeService.registerTrainee(new TraineeRegisterRequest("John", "Doe"));

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
                new TraineeRegisterRequest("John", "Doe",
                        LocalDate.ofYearDay(2001,1), "address1")
        );

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("address1", result.getAddress());
        assertEquals(LocalDate.ofYearDay(2001,1), result.getDateOfBirth());

        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    public void testToggleActivationOfAccount() {
        boolean traineeActiveBefore = trainee.isActive();

        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee newTrainee = traineeService.toggleActivationOfAccount(traineeAuth.username());

        assertNotNull(newTrainee);

        assertEquals(!traineeActiveBefore, newTrainee.isActive());

        verify(traineeRepository).findByUsername(traineeAuth.username());

        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testListTrainees() {
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));

        List<Trainee> result = traineeService.listTrainees();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(traineeRepository).findAll();
    }

    @Test
    void testShowTraineeProfile() {
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.of(trainee));

        TraineeResponse result = traineeService.showProfile(traineeAuth.username());

        assertNotNull(result);

        verify(traineeRepository).findByUsername(traineeAuth.username());
    }

    @Test
    void testUpdateTrainee() {
        TraineeUpdateRequest request = new TraineeUpdateRequest(
                traineeAuth, "John.Doe",
                "John", "NewLastName",
                LocalDate.ofYearDay(2001,1),
                "New Address", true );

        Trainee updTrainee = trainee;
        updTrainee.setLastName(request.lastName());
        updTrainee.setAddress(request.address());

        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        TraineeResponse result = traineeService.updateTrainee(request);

        assertNotNull(result);
        assertEquals("John", result.firstName());
        assertEquals("NewLastName", result.lastName());
        assertEquals("New Address", result.address());
        assertEquals(LocalDate.of(2001, 1, 1), result.dateOfBirth());

        verify(traineeRepository).findByUsername(traineeAuth.username());
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    public void testChangePassword(){
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        String newPassword = "newPassword123";

        Trainee changedPassword = traineeService.changePassword(traineeAuth.username(), newPassword);

        assertNotNull(changedPassword);
        assertEquals(newPassword, changedPassword.getPassword());

        verify(traineeRepository).findByUsername(traineeAuth.username());
    }

    @Test
    public void testChangePasswordWrongPasswordLength(){
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));

        String newPasswordThatShort = "newPa";

        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword(traineeAuth.username(), newPasswordThatShort));

        verify(traineeRepository).findByUsername(traineeAuth.username());
        verify(traineeRepository, never()).save(any(Trainee.class));
    }

    @Test
    public void testChangePasswordRepeatsOldPassword(){
        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));

        String newPasswordThatRepeatsOld = "password1";

        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword(traineeAuth.username(), newPasswordThatRepeatsOld));

        verify(traineeRepository).findByUsername(traineeAuth.username());
        verify(traineeRepository, never()).save(any(Trainee.class));
    }

    @Test
    void testUpdateTrainerList(){
        List<String> trainerUsernames = List.of("Jane.Doe");

        when(traineeRepository.findByUsername(traineeAuth.username())).thenReturn(Optional.ofNullable(trainee));
        when(trainerRepository.findByUsernames(trainerUsernames)).thenReturn(List.of(InitModels.initTrainer()));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        List<TrainerResponse> responses = traineeService.updateTrainerList(traineeAuth.username(), trainerUsernames);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(InitModels.initTrainer().getFirstName(), trainee.getTrainers().get(0).getFirstName());

        verify(traineeRepository).findByUsername(traineeAuth.username());
        verify(traineeRepository).save(any(Trainee.class));
    }
}