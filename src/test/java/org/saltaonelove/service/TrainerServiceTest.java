package org.saltaonelove.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.saltaonelove.InitModels;
import org.saltaonelove.dto.AuthRequest;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.TrainingTypeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private UserCredentialsService userUtil;

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
        TrainerDTO trainerDTO = new TrainerDTO("Jane", "Doe", "Cardio");

        when(trainingTypeRepository.findByName(trainerDTO.specialization())).thenReturn(Optional.of(InitModels.initTrainingType()));
        when(userUtil.generateUsername(any(Trainer.class))).thenReturn("Jane.Doe");
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer result = trainerService.registerTrainer(trainerDTO);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("Cardio", result.getSpecialization().getName());

        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testLogin() {
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.ofNullable(trainer));

        trainerService.loginForTrainer(trainerAuth);

        verify(trainerRepository).findByUsername(trainerAuth.username());
    }

    @Test
    public void testToggleActivationOfAccount() {
        boolean trainerActiveBefore = trainer.isActive();

        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer newTrainer = trainerService.toggleActivationOfAccount(trainerAuth);

        assertNotNull(newTrainer);

        assertEquals(!trainerActiveBefore, newTrainer.isActive());

        verify(trainerRepository, times(2)).findByUsername(trainerAuth.username());

        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testLoginWrongPassword() {
        AuthRequest wrongPasswordAuth = new AuthRequest(trainerAuth.username(), "1337S1mple");

        when(trainerRepository.findByUsername(wrongPasswordAuth.username())).thenReturn(Optional.ofNullable(trainer));

        assertThrows(IllegalArgumentException.class, () -> trainerService.loginForTrainer(wrongPasswordAuth));

        verify(trainerRepository).findByUsername(trainerAuth.username());
    }

    @Test
    void testUpdateTrainer() {
        TrainerDTO updTrainerDTO = new TrainerDTO("Jane", "Down", "Cardio");

        when(trainingTypeRepository.findByName(updTrainerDTO.specialization())).thenReturn(Optional.of(InitModels.initTrainingType()));
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer result = trainerService.updateTrainer(trainerAuth, updTrainerDTO);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Down", result.getLastName());
        assertEquals("Cardio", result.getSpecialization().getName());

        verify(trainerRepository, times(2)).findByUsername(trainerAuth.username());
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

        verify(trainerRepository, times(2)).findByUsername(trainerAuth.username());
    }

    @Test
    public void testChangePasswordWrongPasswordLength(){
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.ofNullable(trainer));

        String newPasswordThatShort = "newPass";

        assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(trainerAuth, newPasswordThatShort));

        verify(trainerRepository, times(2)).findByUsername(trainerAuth.username());
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    public void testChangePasswordRepeatsOldPassword(){
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.ofNullable(trainer));

        String newPasswordThatRepeatsOld = "password123";

        assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(trainerAuth, newPasswordThatRepeatsOld));

        verify(trainerRepository, times(2)).findByUsername(trainerAuth.username());
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void testListTrainers() {
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.ofNullable(trainer));

        List<Trainer> result = trainerService.listTrainers(trainerAuth);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(trainerRepository).findAll();
    }

    @Test
    void testShowTrainerProfile() {
        when(trainerRepository.findByUsername(trainerAuth.username())).thenReturn(Optional.of(trainer));

        Trainer result = trainerService.showProfile(trainerAuth);

        assertNotNull(result);
        assertEquals(2L, result.getUserId());

        verify(trainerRepository, times(2)).findByUsername(trainerAuth.username());
    }
}