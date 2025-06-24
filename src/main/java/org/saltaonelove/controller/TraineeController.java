package org.saltaonelove.controller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.saltaonelove.dto.auth.AuthResponse;
import org.saltaonelove.dto.trainee.TraineeRegisterRequest;
import org.saltaonelove.dto.trainee.TraineeResponse;
import org.saltaonelove.dto.trainee.TraineeUpdateRequest;
import org.saltaonelove.dto.trainee.TraineeUpdateTrainersRequest;
import org.saltaonelove.dto.trainer.TrainerResponse;
import org.saltaonelove.dto.training.TrainingResponse;
import org.saltaonelove.gymshared.util.logging.LoggingUtil;
import org.saltaonelove.gymshared.util.logging.annotation.LogRestCall;
import org.saltaonelove.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainee")
@LogRestCall
@Tag(name = "Trainee", description = "Trainee operations")
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
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeService.registerTrainee(trainee));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainee by username", tags = {"Trainee"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<TraineeResponse> getTraineeByUsername(
            @PathVariable String username) {
        return ResponseEntity.ok().body(traineeService.showProfile(username));
    }

    @GetMapping("/{username}/trainers-available")
    @Operation(summary = "Get trainers available for trainee", tags = {"Trainee"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<List<TrainerResponse>> getTrainersAvailableForTrainee(
            @PathVariable String username) {
        return ResponseEntity.ok().body(traineeService.getTrainersAvailableForTrainee(username));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainee's trainings", tags = {"Trainee"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINEE')")
    @Timed(value="api_endpoint_getTraineeTrainings_time",description="Time to Get All trainee trainings")
    public ResponseEntity<List<TrainingResponse>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType) {
        return ResponseEntity.ok().body(traineeService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainee profile", tags = {"Trainee"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<TraineeResponse> updateTraineeProfile(
            @RequestBody @Valid TraineeUpdateRequest trainee) {
        return ResponseEntity.ok().body(traineeService.updateTrainee(trainee));
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update trainee trainers list", tags = {"Trainee"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<List<TrainerResponse>> updateTraineeTrainers(
            @PathVariable String username,
            @RequestBody @Valid TraineeUpdateTrainersRequest traineeTrainers) {
        return ResponseEntity.ok().body(traineeService.updateTrainerList(username, traineeTrainers.trainersList()));
    }

    @PatchMapping("/{username}/activation")
    @Operation(summary = "Toggle activation of user (Activate/Deactivate)", tags = {"Trainee"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<Void> toggleActivationTrainee(
            @PathVariable String username) {
        traineeService.toggleActivationOfAccount(username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainee profile", tags = {"Trainee"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @PreAuthorize("hasRole('TRAINEE')")
    public ResponseEntity<Void> deleteTraineeProfile(
            @PathVariable String username) {
        traineeService.deleteTrainee(username);
        return ResponseEntity.ok().build();
    }


}
