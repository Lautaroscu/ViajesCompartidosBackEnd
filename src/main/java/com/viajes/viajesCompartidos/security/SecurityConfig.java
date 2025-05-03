    package com.viajes.viajesCompartidos.security;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.context.SecurityContextHolderFilter;


    import static org.springframework.security.config.Customizer.withDefaults;


    @EnableWebSecurity
    @Configuration
    public class SecurityConfig {
        private final InternalRequestFilter internalRequestFilter;
        @Autowired

        public SecurityConfig( InternalRequestFilter internalRequestFilter) {
            this.internalRequestFilter = internalRequestFilter;
        }


        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }




        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorizeRequests -> {
                        authorizeRequests.anyRequest().permitAll();
                    })
                    .cors(withDefaults())
                    .addFilterBefore(internalRequestFilter, SecurityContextHolderFilter.class);

            return http.build();
        }



    }
