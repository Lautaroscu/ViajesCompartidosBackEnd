package com.viajes.viajesCompartidos.models;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationModel implements Serializable {
    private long id;
    private int userId;

    private NotificationType notificationType;

    private String title;
    private String message;

    private String buttonTitle;


    private String actionData;

    private List<String> recipientEmails;


    private LocalDateTime createdAt;
}
