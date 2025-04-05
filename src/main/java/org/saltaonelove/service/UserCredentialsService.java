package org.saltaonelove.service;

import org.saltaonelove.dto.auth.AuthRequest;
import org.saltaonelove.dto.auth.ChangeLoginRequest;
import org.saltaonelove.model.User;
import org.saltaonelove.repos.UserRepository;
import org.saltaonelove.util.logging.annotation.TransactionalWithLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.function.Supplier;

@Service
public class UserCredentialsService {

    private static final Logger log = LoggerFactory.getLogger(UserCredentialsService.class);

    private UserRepository userRepository;

    public UserCredentialsService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public <E extends User> E authorize(AuthRequest auth, Supplier<E> identityProvider) {
        log.info("User login attempt: {}", auth.username());
        E user = identityProvider.get();
        if (auth.password().equals(user.getPassword()) && auth.username().equals(user.getUsername())) {
            log.info("Logged in user: {}", auth.username());
            return user;
        }
        log.warn("Login attempt failed for user: {}", auth.username());
        throw new IllegalArgumentException("Invalid credentials");
    }

    public User login(AuthRequest auth) {
        return authorize(auth, () -> userRepository.findByUsername(auth.username()).get());
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
