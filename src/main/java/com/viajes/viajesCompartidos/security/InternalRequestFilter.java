package com.viajes.viajesCompartidos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/api/**")
public class InternalRequestFilter extends OncePerRequestFilter {

    @Value("${internal.secret}")
    private String internalSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String incomingSecret = request.getHeader("X-Internal-Secret");
        System.out.println(incomingSecret);

        if (incomingSecret == null || !incomingSecret.equals(internalSecret)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        chain.doFilter(request, response);
    }
}
