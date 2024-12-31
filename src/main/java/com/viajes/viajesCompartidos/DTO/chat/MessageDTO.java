package com.viajes.viajesCompartidos.DTO.chat;

import com.viajes.viajesCompartidos.entities.Message;
import com.viajes.viajesCompartidos.entities.User;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageDTO implements Serializable {
    private int id;
    private int userId;
    private String user;
    private String content;
    private LocalDateTime dateTime;
    public MessageDTO() {}
    public MessageDTO(int id, int userId, int chatId, String message, LocalDateTime dateTime) {
        this.id = id;
        this.userId = userId;
        this.content = message;
        this.dateTime = dateTime;
    }
    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.dateTime = message.getTimestamp();
        this.userId = message.getUser().getUserId();
        this.user = message.getUser().getFirstName() + " " + message.getUser().getLastName();
    }

    public int getId() {
        return id;
    }



    public int getUserId() {
        return userId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
