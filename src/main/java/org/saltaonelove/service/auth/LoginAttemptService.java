package org.saltaonelove.service.auth;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPT = 3;
    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> blockedUntil = new ConcurrentHashMap<>();

    private final long BLOCK_TIME = 5 * 60 * 1000;

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        blockedUntil.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);
        if (attempts >= MAX_ATTEMPT) {
            blockedUntil.put(key, System.currentTimeMillis() + BLOCK_TIME);
        }
    }

    public boolean isBlocked(String key) {
        Long blockedTime = blockedUntil.get(key);
        if (blockedTime == null) return false;
        if (blockedTime < System.currentTimeMillis()) {
            blockedUntil.remove(key);
            attemptsCache.remove(key);
            return false;
        }
        return true;
    }
}