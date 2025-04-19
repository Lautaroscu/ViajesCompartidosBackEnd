package com.viajes.viajesCompartidos.DTO;

import com.viajes.viajesCompartidos.entities.JoinRequest;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDTO implements Serializable {
    private Long requestId;
    private Integer userId;
    private String userName;
    private BigDecimal userValoration;
    private String message;
    private String userLastName;
    private Integer tripId;
    private RequestStatus status;
    private LocalDateTime requestDate;

    public JoinRequestDTO(JoinRequest joinRequest) {
        this.requestId = joinRequest.getId();
        this.userId = joinRequest.getUser().getUserId();
        this.tripId = joinRequest.getTrip().getTripId();
        this.status = joinRequest.getStatus();
        this.requestDate = joinRequest.getRequestDate();
        this.userName = joinRequest.getUser().getFirstName();
        this.userLastName = joinRequest.getUser().getLastName();
        this.userValoration = joinRequest.getUser().getValoration();
        this.message = joinRequest.getMessage();

    }



}
