package org.saltaonelove.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.auth.AuthResponse;
import org.saltaonelove.dto.auth.ChangeLoginRequest;
import org.saltaonelove.service.auth.LoginAttemptService;
import org.saltaonelove.service.UserCredentialsService;
import org.saltaonelove.util.auth.LoginAttemptUtils;
import org.saltaonelove.util.logging.annotation.LogRestCall;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@LogRestCall
public class AuthController {

    private UserCredentialsService userCredentialsService;
    private LoginAttemptUtils loginAttemptUtils;
    private LoginAttemptService loginAttemptService;


    public AuthController(UserCredentialsService userCredentialsService, LoginAttemptUtils loginAttemptUtils, LoginAttemptService loginAttemptService) {
        this.userCredentialsService = userCredentialsService;
        this.loginAttemptUtils = loginAttemptUtils;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid AuthRequest auth, HttpServletRequest request) {
        checkLoginAttempt(request);
        return ResponseEntity.status(HttpStatus.OK).body(userCredentialsService.login(auth));
    }

    @PutMapping("/change-password/{username}")
    @Operation(summary = "Change password", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<Void> changePassword(
            @PathVariable String username,
            @Valid @RequestBody ChangeLoginRequest changeLoginRequest) {
        userCredentialsService.changeLogin(username, changeLoginRequest);
        return ResponseEntity.ok().build();
    }

    private void checkLoginAttempt(HttpServletRequest request) {
        String ip = loginAttemptUtils.getClientIP(request);
        if (loginAttemptService.isBlocked(ip)) {
            throw new LockedException("Too many failed attempts. Try again later.");
        }
    }
}
