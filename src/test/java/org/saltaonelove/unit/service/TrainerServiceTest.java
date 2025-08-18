package org.saltaonelove.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.clients.workload.WorkloadClient;
import org.saltaonelove.model.dto.auth.AuthRequest;
import org.saltaonelove.model.dto.auth.AuthResponse;
import org.saltaonelove.model.dto.trainer.TrainerRequest;
import org.saltaonelove.model.dto.trainer.TrainerResponse;
import org.saltaonelove.model.dto.trainer.TrainerUpdateRequest;
import org.saltaonelove.model.entity.Trainer;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.TrainingTypeRepository;
import org.saltaonelove.service.TrainerService;
import org.saltaonelove.service.UserCredentialsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCredentialsService userUtil;

    @Mock
    private WorkloadClient workloadClient;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;
    private AuthRequest trainerAuth;

    @BeforeEach
    void setUp() {
        trainer = InitModels.initTrainer();
        trainerAuth = new AuthRequest(trainer.getUsername(), trainer.getPassword());
    }

    @Test
    void testRegisterTrainer() {
        TrainerRequest trainerRequest = new TrainerRequest("Jane", "Doe", "Cardio");

        when(trainingTypeRepository.findByName(trainerRequest.specialization())).thenReturn(Optional.of(InitModels.initTrainingType()));
        when(userUtil.generateUsername(any(Trainer.class))).thenReturn("Jane.Doe");
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        when(passwordEncoder.encode(any())).thenReturn("somepasswordinhashwithsalt");

        AuthResponse result = trainerService.registerTrainer(trainerRequest);

        assertNotNull(result);
        assertEquals("Jane.Doe", result.username());

        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    public void testToggleActivationOfAccount() {
        boolean trainerActiveBefore = trainer.isActive();

        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer newTrainer = trainerService.toggleActivationOfAccount(trainerAuth.username());

        assertNotNull(newTrainer);

        assertEquals(!trainerActiveBefore, newTrainer.isActive());

        verify(trainerRepository).findByUsername(trainerAuth.username());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testUpdateTrainer() {
        TrainerUpdateRequest updTrainerRequest = new TrainerUpdateRequest("Jane.Doe","Jane", "Down", "Cardio", true);

        when(trainingTypeRepository.findByName(updTrainerRequest.specialization())).thenReturn(Optional.of(InitModels.initTrainingType()));
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        TrainerResponse result = trainerService.updateTrainer("Jane.Doe", updTrainerRequest);

        assertNotNull(result);
        assertEquals("Jane", result.firstName());
        assertEquals("Down", result.lastName());
        assertEquals("Cardio", result.specialization().getName());

        verify(trainerRepository).findByUsername(trainerAuth.username());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    public void testChangePassword(){
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.ofNullable(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        String newPassword = "newPassword123";

        Trainer changedPassword = trainerService.changePassword(trainerAuth, newPassword);

        assertNotNull(changedPassword);
        assertEquals(newPassword, changedPassword.getPassword());

        verify(trainerRepository).findByUsername(trainerAuth.username());
    }

    @Test
    public void testChangePasswordWrongPasswordLength(){
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.ofNullable(trainer));

        String newPasswordThatShort = "newPass";

        assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(trainerAuth, newPasswordThatShort));

        verify(trainerRepository).findByUsername(trainerAuth.username());
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    public void testChangePasswordRepeatsOldPassword(){
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.ofNullable(trainer));

        String newPasswordThatRepeatsOld = "password123";

        assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(trainerAuth, newPasswordThatRepeatsOld));

        verify(trainerRepository).findByUsername(trainerAuth.username());
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void testListTrainers() {
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));

        List<Trainer> result = trainerService.listTrainers(trainerAuth.username());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(trainerRepository).findAll();
    }

    @Test
    void testShowTrainerProfile() {
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.of(trainer));

        TrainerResponse result = trainerService.showProfile(trainerAuth.username());

        assertNotNull(result);

        verify(trainerRepository).findByUsername(trainerAuth.username());
    }

    @Test
    void testDeleteTrainer() {
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.ofNullable(trainer));

        trainerService.deleteTrainer(trainerAuth.username());

        verify(trainerRepository).findByUsername(trainerAuth.username());
        verify(workloadClient).deleteTrainerWorkloadHistory(any(String.class));
    }
}