package com.viajes.viajesCompartidos.security;

import com.viajes.viajesCompartidos.services.LoginAttemptService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, java.io.IOException {
        String username = request.getParameter("email"); // Asume que el nombre de usuario se pasa como parámetro
        loginAttemptService.loginFailed(username); // Incrementar los intentos fallidos

        if (loginAttemptService.isBlocked(username)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You have been locked out due to too many failed login attempts.");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password.");
        }
    }
}
