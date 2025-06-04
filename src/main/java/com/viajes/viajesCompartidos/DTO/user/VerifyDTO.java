package com.viajes.viajesCompartidos.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
public class VerifyDTO implements Serializable {
    private boolean verified;

    public VerifyDTO(){}
    public VerifyDTO(boolean verified) {
        this.verified = verified;
    }
}
