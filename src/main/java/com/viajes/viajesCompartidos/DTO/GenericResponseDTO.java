package com.viajes.viajesCompartidos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponseDTO implements Serializable {
    private boolean success;
    private String message;
}
