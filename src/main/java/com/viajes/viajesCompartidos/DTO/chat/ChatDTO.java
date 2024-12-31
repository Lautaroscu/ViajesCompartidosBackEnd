package com.viajes.viajesCompartidos.DTO.chat;

import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.entities.Chat;
import com.viajes.viajesCompartidos.entities.Message;
import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.entities.User;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ChatDTO implements Serializable {
    private List<MessageDTO> messages;
    private int id;
    private int tripId;
    private List<OutputUserDTO> integrants;
    public ChatDTO() {}
    public ChatDTO(List<MessageDTO> messages, int id, List<OutputUserDTO> integrants , int tripId) {
        this.messages = messages;
        this.id = id;
        this.integrants = integrants;
        this.tripId = tripId;
    }
    public ChatDTO(Chat chat) {
        id = chat.getId();
        this.tripId = chat.getTrip().getTripId();
        integrants = chat.getTrip().getPassengers().stream().map(OutputUserDTO::new).collect(Collectors.toList());
        OutputUserDTO ownerDTO = new OutputUserDTO(chat.getTrip().getOwner());
        integrants.add(ownerDTO);
        messages = chat.getMessages().stream().map(MessageDTO::new).collect(Collectors.toList());
    }
    public int getId() {
        return id;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public List<OutputUserDTO> getIntegrants() {
        return integrants;
    }
}
