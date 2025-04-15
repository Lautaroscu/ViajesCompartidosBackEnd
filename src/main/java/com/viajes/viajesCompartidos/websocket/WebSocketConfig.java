package com.viajes.viajesCompartidos.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configuración del broker de mensajes
        config.enableSimpleBroker("/topic/chat"); // Canal para enviar mensajes
        config.setApplicationDestinationPrefixes("/app"); // Prefijo para enviar mensajes desde cliente al servidor

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:5173" , "https://rideshared.netlify.app"); // Cambia según el dominio del frontend


    }
}
