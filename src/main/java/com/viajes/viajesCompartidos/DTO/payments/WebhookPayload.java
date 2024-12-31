package com.viajes.viajesCompartidos.DTO.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@ToString
public class WebhookPayload implements Serializable {
        private String action;
        private String api_version;
        private Map<String, Object> data;
        private String date_created;
        private Long id;
        private boolean live_mode;
        private String type;
        private long user_id;

        // Getters y setters
    }


