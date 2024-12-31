package com.viajes.viajesCompartidos.DTO.chat;

public class InputMessageDTO {
    private String content;
    private Integer userId;
    private Integer chatId;
    public InputMessageDTO() {}
    public InputMessageDTO(String content, Integer userId, Integer chatId) {
        this.content = content;
        this.userId = userId;
        this.chatId = chatId;

    }

    public Integer getChatId() {
        return chatId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
