package com.viajes.viajesCompartidos.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    private final static int MAX_ATTEMPT = 5; // Máximo de intentos fallidos permitidos
    private final static long LOCK_TIME_DURATION = 30 * 60 * 1000; // 30 minutos
    private final Map<String, Integer> attemptsCache = new HashMap<>();
    private final Map<String, Long> lockTimeCache = new HashMap<>();

    public boolean isBlocked(String username) {
        Long lockTime = lockTimeCache.get(username);
        if (lockTime != null && (System.currentTimeMillis() - lockTime < LOCK_TIME_DURATION)) {
            return true; // El usuario está bloqueado
        }
        return false; // El usuario no está bloqueado
    }

    public void loginSucceeded(String username) {
        attemptsCache.remove(username); // El usuario ha iniciado sesión correctamente
    }

    public void loginFailed(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0);
        attempts++;
        if (attempts >= MAX_ATTEMPT) {
            lockTimeCache.put(username, System.currentTimeMillis()); // Bloquear el usuario por un tiempo
        }
        attemptsCache.put(username, attempts);
    }
}
