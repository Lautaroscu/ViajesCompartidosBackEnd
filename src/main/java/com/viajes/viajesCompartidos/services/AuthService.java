package com.viajes.viajesCompartidos.services;

import com.viajes.viajesCompartidos.DTO.auth.InputAuthDTO;
import com.viajes.viajesCompartidos.DTO.auth.InputRegisterDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.InvalidCredentialsException;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil; // Asegúrate de tener tu JwtUtil configurado
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

            @Autowired
            public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
                this.authenticationManager = authenticationManager;
                this.jwtUtil = jwtUtil;
                this.userRepository = userRepository;
                this.passwordEncoder = new BCryptPasswordEncoder();
            }

    public void register(InputRegisterDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        if (invalidRequest(userDTO)) {
            throw new BadRequestException("Invalid user, check the fields and try again");
        }
        if (!equalsPasswords(userDTO.getPassword(), userDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = new User(userDTO.getName(), userDTO.getLastName(), userDTO.getPhoneNumber(), userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
    }

    public String authenticate(InputAuthDTO request) {
        try {
            System.out.println(request.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtUtil.generateToken(userDetails);

        } catch (AuthenticationException e) {
            // Manejar errores de autenticación
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }


    private boolean invalidRequest(InputRegisterDTO request) {
        return request.getEmail() == null || request.getEmail().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty()
                || request.getName() == null || request.getName().isEmpty() || request.getLastName() == null || request.getLastName().isEmpty() || request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty() ||
                request.getConfirmPassword() == null || request.getConfirmPassword().isEmpty();
    }

    private boolean equalsPasswords(String password1, String password2) {
        return password2.equals(password1);
    }


public boolean tokenValid(String token) {
                return jwtUtil.validateToken(token);
}

}
