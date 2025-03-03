package org.saltaonelove.util;

import org.saltaonelove.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class UserUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(UserUtil.class);

    public String generateUsername(User user, List<User> existingUsers) {
        String baseUsername = user.getFirstName() + "." + user.getLastName(); // Основное имя без чисел

        // filter with base username
        var matchingNames = existingUsers.stream()
                .map(User::getUsername)
                .filter(name -> name.startsWith(baseUsername))
                .toList();

        // no same names with basename -> return baseusername
        if (matchingNames.isEmpty()) {
            return baseUsername;
        }

        var serials = matchingNames.stream()
                .map(name -> name.substring(baseUsername.length())) // leave suffixes
                .filter(n -> !n.isEmpty()) // skip empty strings
                .mapToInt(Integer::parseInt) // mapToInt
                .max() // max
                .orElse(0); // if not found max -> 0

        // generate with incremented
        return baseUsername + (serials + 1);
    }

    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

}
