package org.saltaonelove.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.auth.ChangeLoginRequest;
import org.saltaonelove.model.User;
import org.saltaonelove.service.UserCredentialsService;
import org.saltaonelove.util.logging.annotation.LogRestCall;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@LogRestCall
public class AuthController {

    private UserCredentialsService userCredentialsService;

    public AuthController(UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    @GetMapping
    @Operation(summary = "Login user", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = AuthRequest.class))
    ))
    public ResponseEntity<Void> login(
            @RequestBody @Valid AuthRequest auth) {
        userCredentialsService.login(auth);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}")
    @Operation(summary = "Change password")
    public ResponseEntity<Void> changePassword(
            @PathVariable String username,
            @Valid @RequestBody ChangeLoginRequest changeLoginRequest) {
        userCredentialsService.changeLogin(username, changeLoginRequest);
        return ResponseEntity.ok().build();
    }

}
