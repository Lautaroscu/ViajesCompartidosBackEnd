package com.viajes.viajesCompartidos.clients;

import com.viajes.viajesCompartidos.models.NotificationModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notifications-service" , url = "${notifications.url}")
public interface NotificationsClient {

    @PostMapping("/notifications")
    void sendNotification(@RequestBody NotificationModel notification);
}
