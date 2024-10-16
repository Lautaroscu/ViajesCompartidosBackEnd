package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.auth.InputAuthDTO;

import com.viajes.viajesCompartidos.DTO.auth.InputRegisterDTO;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.InvalidCredentialsException;

import com.viajes.viajesCompartidos.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody InputRegisterDTO userDTO) {
        try {
            authService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody InputAuthDTO request , HttpServletResponse response) {
        try {
            String token = authService.authenticate(request);
            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setHttpOnly(false); // Evita el acceso desde JavaScript
            jwtCookie.setSecure(false); // Solo se envía por HTTPS
            jwtCookie.setPath("/"); // Disponible en toda la app
            jwtCookie.setMaxAge(24 * 60 * 60); // Tiempo de vida en segundos (1 día)

            response.addCookie(jwtCookie);
            System.out.println("Generated token: " + token);
            System.out.println("Cookie added: " + jwtCookie.getName() + " = " + jwtCookie.getValue());


            return ResponseEntity.ok("Authenticated successfully!");
        } catch (InvalidCredentialsException e) {
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
                    cookie.setHttpOnly(false);
                    cookie.setSecure(false); // Cambia a true si usas HTTPS
                    cookie.setPath("/"); // Asegúrate de que la ruta coincida con la original
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        return ResponseEntity.ok("Logged out successfully!");
    }





}

