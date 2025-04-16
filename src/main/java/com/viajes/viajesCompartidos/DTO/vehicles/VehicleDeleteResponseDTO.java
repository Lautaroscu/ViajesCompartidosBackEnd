package com.viajes.viajesCompartidos.DTO.vehicles;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor

public class VehicleDeleteResponseDTO implements Serializable {
    private String plateOld;
    private String plateNewPred;
    private boolean deleted;
    private boolean wasLastVehicle;
    public VehicleDeleteResponseDTO() {}


}