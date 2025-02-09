package com.viajes.viajesCompartidos.DTO;

import com.viajes.viajesCompartidos.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusDTO implements Serializable {
    private RequestStatus status;
}
