package com.viajes.viajesCompartidos.DTO.chat;

import com.viajes.viajesCompartidos.entities.Message;
import com.viajes.viajesCompartidos.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor

public class MessageDTO implements Serializable {
    private int id;
    private int userId;
    private String user;
    private String content;
    private LocalDateTime dateTime;
    private int chatId;
    public MessageDTO() {}

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.dateTime = message.getTimestamp();
        this.userId = message.getUser().getUserId();
        this.user = message.getUser().getFirstName() + " " + message.getUser().getLastName();
        this.chatId = message.getChat().getId();
    }
}
