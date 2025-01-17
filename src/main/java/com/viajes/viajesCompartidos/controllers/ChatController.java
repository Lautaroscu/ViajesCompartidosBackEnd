package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.chat.InputMessageDTO;
import com.viajes.viajesCompartidos.DTO.chat.MessageDTO;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
    }

    @MessageMapping("/sendMessage/{chatId}")
    @SendTo("/topic/messages/{chatId}")
    public MessageDTO sendMessage(@DestinationVariable Integer chatId, InputMessageDTO messageDTO) {
        try {
            return chatService.sendMessage(messageDTO);

        } catch (TripNotFoundException e) {
            throw new TripNotFoundException(e.getMessage());
        }
    }
}

