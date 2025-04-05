package org.saltaonelove.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.auth.AuthResponse;
import org.saltaonelove.dto.trainer.TrainerRequest;
import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.dto.trainer.TrainerUpdateRequest;
import org.saltaonelove.dto.training.TrainingResponse;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.service.TrainerService;
import org.saltaonelove.util.logging.annotation.LogRestCall;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainer")
@LogRestCall
public class TrainerController {

    private TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    @Operation(summary = "Register a trainer")
    public ResponseEntity<AuthResponse> registerTrainer(
            @RequestBody @Valid TrainerRequest trainer) {
        Trainer newTrainer = trainerService.registerTrainer(trainer);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AuthResponse(newTrainer.getUsername(), newTrainer.getPassword()));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainer by username")
    public ResponseEntity<TrainerResponse> getTrainerByUsername(
            @PathVariable String username,
            @RequestBody @Valid AuthRequest authRequest) {
        trainerService.loginForTrainer(authRequest);
        return ResponseEntity.ok().body(trainerService.showProfile(username));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainer's trainings")
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String traineeName,
            @RequestParam(required = false) String trainingType,
            @RequestBody @Valid AuthRequest authRequest) {
        trainerService.loginForTrainer(authRequest);
        return ResponseEntity.ok().body(trainerService.getTrainerTrainings(username, fromDate, toDate, traineeName, trainingType));

    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainer by username")
    public ResponseEntity<TrainerResponse> updateTrainer(@RequestBody TrainerUpdateRequest trainer){
        trainerService.loginForTrainer(trainer.auth());
        return ResponseEntity.ok().body(trainerService.updateTrainer(trainer));
    }

    @PatchMapping("/{username}/activation")
    public ResponseEntity<Void> toggleActivationTrainee(
            @PathVariable String username,
            @RequestBody AuthRequest authRequest) {
        trainerService.loginForTrainer(authRequest);
        trainerService.toggleActivationOfAccount(username);
        return ResponseEntity.ok().build();
    }


}
