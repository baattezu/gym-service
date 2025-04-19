package org.saltaonelove.service;

import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.auth.AuthResponse;
import org.saltaonelove.dto.auth.ChangeLoginRequest;
import org.saltaonelove.exception.exceptions.AuthException;
import org.saltaonelove.metrics.AuthMetrics;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.User;
import org.saltaonelove.repos.TraineeRepository;
import org.saltaonelove.repos.TrainerRepository;
import org.saltaonelove.repos.UserRepository;
import org.saltaonelove.util.auth.LoginAttemptUtils;
import org.saltaonelove.util.logging.annotation.TransactionalWithLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Service("userCredentialsService")
public class UserCredentialsService{

    private static final Logger log = LoggerFactory.getLogger(UserCredentialsService.class);

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private AuthMetrics authMetrics;

    public UserCredentialsService(
            UserRepository userRepository, AuthenticationManager authenticationManager,
            AuthMetrics authMetrics,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.authMetrics = authMetrics;
        this.jwtService = jwtService;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateUsername(User user) {
        String baseUsername = user.getFirstName() + "." + user.getLastName();
        int baseLength = baseUsername.length();

        List<String> matchingUsernames = userRepository.findUsernamesByBase(baseUsername);

        if (matchingUsernames.isEmpty()) {
            return baseUsername;
        }

        var serials = matchingUsernames.stream()
                .map(name -> name.substring(baseLength))
                .filter(n -> !n.isEmpty())
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);

        return baseUsername + (serials + 1);
    }

    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    public void authorize(AuthRequest auth) {
        log.info("User login attempt: {}", auth.username());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            auth.username(), auth.password()));
            log.info("Logged in user: {}", auth.username());
        } catch (BadCredentialsException ex){
            log.warn("Login attempt failed for user: {}", auth.username());
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    public AuthResponse login(AuthRequest auth) {
        authorize(auth);
        User user = userRepository.findByUsername(auth.username()).get();

        String token = jwtService.generateToken(user);
        authMetrics.onSuccessfulLogin(user.getUserId());

        return new AuthResponse(token);
    }

    @TransactionalWithLogging
    public User changeLogin(String username, ChangeLoginRequest changeLoginRequest) {
        log.info("Change password attempt: {}", changeLoginRequest.username());
        if (!username.equals(changeLoginRequest.username())) {
            throw new IllegalArgumentException("Invalid username");
        }
        User user = userRepository.findByUsername(changeLoginRequest.username()).get();
        if (!changeLoginRequest.oldPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Old password is not correct");
        }

        user.setPassword(changeLoginRequest.newPassword());
        userRepository.save(user);
        log.info("Changed password for user: {}", changeLoginRequest.username());

        return user;
    }
}
