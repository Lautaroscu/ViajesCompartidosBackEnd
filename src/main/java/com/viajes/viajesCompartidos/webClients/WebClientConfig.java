package com.viajes.viajesCompartidos.webClients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://h.api-comp.redlink.com.ar/redlink/homologacion/transferenciasinmediatas/2/0/0") // Base URL del servicio externo
                .build();
    }
}
