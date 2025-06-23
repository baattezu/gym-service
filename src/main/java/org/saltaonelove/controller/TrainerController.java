package org.saltaonelove.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.saltaonelove.dto.auth.AuthResponse;
import org.saltaonelove.dto.trainer.TrainerRequest;
import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.dto.trainer.TrainerUpdateRequest;
import org.saltaonelove.dto.training.TrainingResponse;
import org.saltaonelove.service.TrainerService;
import org.saltaonelove.util.logging.annotation.LogRestCall;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainer")
@LogRestCall
@Tag(name = "Trainer", description = "Trainer operations")
public class TrainerController {

    private TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    @Operation(summary = "Register a trainer")
    public ResponseEntity<AuthResponse> registerTrainer(
            @RequestBody @Valid TrainerRequest trainer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerService.registerTrainer(trainer));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainer by username", tags = {"Trainer"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TrainerResponse> getTrainerByUsername(
            @PathVariable String username) {
        return ResponseEntity.ok().body(trainerService.showProfile(username));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainer's trainings", tags = {"Trainer"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String traineeName,
            @RequestParam(required = false) String trainingType) {
        return ResponseEntity.ok().body(trainerService.getTrainerTrainings(username, fromDate, toDate, traineeName, trainingType));

    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainer by username", tags = {"Trainer"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TrainerResponse> updateTrainer(
            @PathVariable String username,
            @RequestBody TrainerUpdateRequest trainer){
        return ResponseEntity.ok().body(trainerService.updateTrainer(username, trainer));
    }

    @PatchMapping("/{username}/activation")
    @Operation(summary = "Toggle activation of trainer by username", tags = {"Trainer"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Void> toggleActivationTrainee(
            @PathVariable String username) {
        trainerService.toggleActivationOfAccount(username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainer by username", tags = {"Trainer"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TrainerResponse> deleteTrainer(@PathVariable String username){
        trainerService.deleteTrainer(username);
        return ResponseEntity.ok().build();
    }



}
