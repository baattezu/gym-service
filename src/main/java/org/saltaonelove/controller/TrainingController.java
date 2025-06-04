package org.saltaonelove.controller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.saltaonelove.dto.training.TrainingRequest;
import org.saltaonelove.model.TrainingType;
import org.saltaonelove.service.TrainingService;
import org.saltaonelove.util.logging.annotation.LogRestCall;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/training")
@LogRestCall
public class TrainingController {

    private TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    @Operation(summary = "Create training",  security = @SecurityRequirement(name = "Bearer Authentication"))
    @Timed(value="api_endpoint_createTraining_time",description="Time to Create training")
    public ResponseEntity<Void> createTraining(@RequestBody @Valid TrainingRequest trainingRequest){
        trainingService.createTraining(trainingRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/types")
    @Operation(summary = "Get training types",  security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<List<TrainingType>> getTrainingTypes(){
        return ResponseEntity.ok().body(trainingService.getTrainingTypes());
    }

}
