package org.saltaonelove.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.training.TrainingRequest;
import org.saltaonelove.model.TrainingType;
import org.saltaonelove.model.User;
import org.saltaonelove.service.TrainingService;
import org.saltaonelove.service.UserCredentialsService;
import org.saltaonelove.util.logging.annotation.LogRestCall;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training")
@LogRestCall
public class TrainingController {

    private TrainingService trainingService;
    private UserCredentialsService userCredentialsService;

    public TrainingController(TrainingService trainingService, UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
        this.trainingService = trainingService;
    }

    @PostMapping
    @Operation(summary = "Create training")
    public ResponseEntity<Void> createTraining(@RequestBody @Valid TrainingRequest trainingRequest){
        userCredentialsService.login(trainingRequest.authRequest());
        trainingService.createTraining(trainingRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/types")
    @Operation(summary = "Get training types")
    public ResponseEntity<List<TrainingType>> getTrainingTypes(@RequestBody @Valid AuthRequest authRequest){
        userCredentialsService.login(authRequest);
        return ResponseEntity.ok().body(trainingService.getTrainingTypes());
    }

}
