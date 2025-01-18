package com.viajes.viajesCompartidos.profiles;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-prod.properties")
@Profile("production")
public class ProdProfile {
}
