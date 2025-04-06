package com.viajes.viajesCompartidos.DTO.chat;

import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.entities.Chat;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ChatDTO implements Serializable {
    private List<MessageDTO> messages;
    private int id;
    private int tripId;
    private List<String> integrants  = new ArrayList<>();

    public ChatDTO() {
    }

    public ChatDTO(List<MessageDTO> messages, int id, List<String> integrants, int tripId) {
        this.messages = messages;
        this.id = id;
        this.integrants = integrants;
        this.tripId = tripId;
    }

    public ChatDTO(Chat chat) {
        id = chat.getId();
        this.tripId = chat.getTrip().getTripId();

        OutputUserDTO ownerDTO = new OutputUserDTO(chat.getTrip().getOwner());
        integrants = new ArrayList<>();
        integrants.add(ownerDTO.getFirstName() + " " + ownerDTO.getLastName());
        integrants.addAll(chat.getTrip().getPassengers().stream()
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .toList());

        messages = chat.getMessages().stream().map(MessageDTO::new).collect(Collectors.toList());
    }


}
