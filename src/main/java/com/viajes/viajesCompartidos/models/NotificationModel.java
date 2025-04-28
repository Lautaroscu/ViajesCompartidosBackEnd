package com.viajes.viajesCompartidos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationModel implements Serializable {
    private long id;
    private int userId;
    private int tripId;

    private NotificationType notificationType;

    private String title;
    private String message;

    private String buttonTitle;

    private String redirectButtonTitle;

    private String actionData;

    private List<String> recipientEmails;


    private LocalDateTime createdAt;
}
