package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.auth.InputAuthDTO;

import com.viajes.viajesCompartidos.DTO.auth.InputRegisterDTO;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.InvalidCredentialsException;

import com.viajes.viajesCompartidos.services.AuthService;
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
    public ResponseEntity<?> login(@RequestBody InputAuthDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(request));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


}

