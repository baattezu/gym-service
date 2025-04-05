package org.saltaonelove.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.auth.AuthResponse;
import org.saltaonelove.dto.trainee.TraineeRegisterRequest;
import org.saltaonelove.dto.trainee.TraineeResponse;
import org.saltaonelove.dto.trainee.TraineeUpdateRequest;
import org.saltaonelove.dto.trainee.TraineeUpdateTrainersRequest;
import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.dto.training.TrainingResponse;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.service.TraineeService;
import org.saltaonelove.util.logging.LoggingUtil;
import org.saltaonelove.util.logging.annotation.LogRestCall;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainee")
@LogRestCall
public class TraineeController {

    private static final LoggingUtil log = LoggingUtil.getLogger(TraineeController.class);

    private TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    @Operation(summary = "Register a trainee")
    public ResponseEntity<AuthResponse> registerTrainee(
            @RequestBody @Valid TraineeRegisterRequest trainee) {
        Trainee newTrainee = traineeService.registerTrainee(trainee);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AuthResponse(newTrainee.getUsername(), newTrainee.getPassword()));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainee by username")
    public ResponseEntity<TraineeResponse> getTraineeByUsername(
            @PathVariable String username,
            @RequestBody @Valid AuthRequest authRequest) {
        traineeService.loginForTrainee(authRequest);
        return ResponseEntity.ok().body(traineeService.showProfile(username));
    }

    @GetMapping("/{username}/trainers-available")
    @Operation(summary = "Get trainers available for trainee")
    public ResponseEntity<List<TrainerResponse>> getTrainersAvailableForTrainee(
            @PathVariable String username,
            @RequestBody @Valid AuthRequest authRequest) {
        traineeService.loginForTrainee(authRequest);
        return ResponseEntity.ok().body(traineeService.getTrainersAvailableForTrainee(username));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainee's trainings")
    public ResponseEntity<List<TrainingResponse>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType,
            @RequestBody @Valid AuthRequest authRequest) {
        traineeService.loginForTrainee(authRequest);
        return ResponseEntity.ok().body(traineeService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainee profile")
    public ResponseEntity<TraineeResponse> updateTraineeProfile(@RequestBody @Valid TraineeUpdateRequest trainee) {
        return ResponseEntity.ok().body(traineeService.updateTrainee(trainee));
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update trainee trainers list")
    public ResponseEntity<List<TrainerResponse>> updateTraineeTrainers(
            @PathVariable String username,
            @RequestBody @Valid TraineeUpdateTrainersRequest traineeTrainers) {
        traineeService.loginForTrainee(traineeTrainers.auth());
        return ResponseEntity.ok().body(traineeService.updateTrainerList(username, traineeTrainers.trainersList()));
    }

    @PatchMapping("/{username}/activation")
    @Operation(summary = "Toggle activation of user (Activate/Deactivate)")
    public ResponseEntity<Void> toggleActivationTrainee(
            @PathVariable String username,
            @RequestBody @Valid AuthRequest authRequest) {
        traineeService.loginForTrainee(authRequest);
        traineeService.toggleActivationOfAccount(username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainee profile")
    public ResponseEntity<Void> deleteTraineeProfile(
            @PathVariable String username,
            @RequestBody @Valid AuthRequest auth) {
        traineeService.loginForTrainee(auth);
        traineeService.deleteTrainee(username);
        return ResponseEntity.ok().build();
    }


}
