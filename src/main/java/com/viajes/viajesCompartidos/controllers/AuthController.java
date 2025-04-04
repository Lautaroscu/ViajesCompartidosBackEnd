package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.auth.InputAuthDTO;

import com.viajes.viajesCompartidos.DTO.auth.InputRegisterDTO;

import com.viajes.viajesCompartidos.exceptions.InvalidCredentialsException;

import com.viajes.viajesCompartidos.exceptions.TooManyAttemptsException;
import com.viajes.viajesCompartidos.services.AuthService;
import com.viajes.viajesCompartidos.services.LoginAttemptService;
import com.viajes.viajesCompartidos.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;
    private  final UserService userService;

    private final LoginAttemptService loginAttemptService;

    @Autowired
    public AuthController(AuthService authService, UserService userService, LoginAttemptService loginAttemptService) {
        this.authService = authService;
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody InputRegisterDTO userDTO) {
        try {
            authService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("registered", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody InputAuthDTO request, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        try {
            if(loginAttemptService.isBlocked(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Too many attempts, try again later");
            }

            String token = authService.authenticate(request);

            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setHttpOnly(true); // Evita el acceso desde JavaScript
            jwtCookie.setSecure(true); // Solo se envía por HTTPS
            jwtCookie.setPath("/"); // Disponible en toda la app
            jwtCookie.setMaxAge(24 * 60 * 60); // Tiempo de vida en segundos (1 día)
            jwtCookie.setAttribute("SameSite" , "None");

            int userId = userService.getUser(request.getEmail()).getId();


            response.addCookie(jwtCookie);

            loginAttemptService.loginSucceeded(request.getEmail());
            return ResponseEntity.ok(Map.of("authenticated", true , "userId", userId));
        }
        catch (InvalidCredentialsException e) {
            loginAttemptService.loginFailed(request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    // Establece la cookie con valor vacío, maxAge 0 para eliminarla
                    cookie.setValue(null);
                    cookie.setMaxAge(0); // Expira inmediatamente
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true); // Cambia a true si usas HTTPS
                    cookie.setPath("/"); // Asegúrate de que la ruta coincida con la original
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        return ResponseEntity.ok(Map.of("unauthenticated", true));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    try {
                        // Validar el token

                        return ResponseEntity.ok(Map.of("authenticated", authService.tokenValid(token)));
                    } catch (ExpiredJwtException e) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not found");
    }


}

