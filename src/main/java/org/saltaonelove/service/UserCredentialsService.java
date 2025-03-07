package org.saltaonelove.service;

import org.saltaonelove.dao.TraineeDAO;
import org.saltaonelove.dao.TrainerDAO;
import org.saltaonelove.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserCredentialsService {

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private TrainerDAO trainerDAO;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateUsername(User user) {
        String baseUsername = user.getFirstName() + "." + user.getLastName();

        var matchingNames = loadUsers().stream()
                .map(User::getUsername)
                .filter(name -> name.startsWith(baseUsername))
                .toList();

        if (matchingNames.isEmpty()) {
            return baseUsername;
        }

        var serials = matchingNames.stream()
                .map(name -> name.substring(baseUsername.length()))
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

    private List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(trainerDAO.findAll());
        users.addAll(traineeDAO.findAll());
        return users;
    }
}
