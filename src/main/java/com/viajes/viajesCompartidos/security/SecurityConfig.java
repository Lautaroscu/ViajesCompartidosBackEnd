    package com.viajes.viajesCompartidos.security;

    import com.viajes.viajesCompartidos.services.CustomUserDetailsService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
    import org.springframework.web.servlet.config.annotation.CorsRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    import java.util.List;

    import static org.springframework.security.config.Customizer.withDefaults;


    @EnableWebSecurity
    @Configuration
    public class SecurityConfig {
        private final JwtFilter jwtFilter;
        private final CustomUserDetailsService customUserDetailsService;
        private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
        @Autowired

        public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService customUserDetailsService , CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
            this.jwtFilter = jwtFilter;
            this.customUserDetailsService = customUserDetailsService;
            this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        }


        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
            AuthenticationManagerBuilder authenticationManagerBuilder =
                    http.getSharedObject(AuthenticationManagerBuilder.class);

            // Configurar UserDetailsService y PasswordEncoder
            authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

            return authenticationManagerBuilder.build();
        }


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http , CorsConfigurationSource corsConfigurationSource) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // Desactiva CSRF usando la nueva API
                    .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/api/recharges/webhook").permitAll()
                                    .requestMatchers("/api/geonames/**").permitAll()
                                    .requestMatchers("/chat/**").permitAll()// Permitir acceso sin autenticación para /api/auth/**
                            .anyRequest().authenticated()
                            // Requiere autenticación para otras solicitudes
                    )
                    .cors(cors -> cors.configurationSource(corsConfigurationSource))
                    // Usar configuración de CORS
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


            return http.build();
        }

//        @Bean
//        public CorsConfigurationSource corsConfigurationSource() {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(List.of("http://localhost:5173" , "http://localhost:8080"  ,"https://rideshared.netlify.app" ,"https://www.mercadopago.com.ar")); // Frontend React
//            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS" , "PATCH"));
//            configuration.setAllowedHeaders(List.of("*"));
//            configuration.setAllowCredentials(true); // Permitir cookies
//
//
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", configuration);
//            return source;
//        }


    }
