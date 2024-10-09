package com.viajes.viajesCompartidos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**") // Permite todas las rutas
                    .allowedOriginPatterns("http://localhost:*") // Permitir todos los puertos en localhost
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*") // Permite todos los headers
                    .allowCredentials(true); // Permite credenciales (como cookies)
        }

}
