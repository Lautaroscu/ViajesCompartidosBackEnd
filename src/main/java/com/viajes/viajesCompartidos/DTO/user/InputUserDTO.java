package com.viajes.viajesCompartidos.DTO.user;

import com.viajes.ViajesCompartidos.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class InputUserDTO implements Serializable {
    private String name;
    private String lastName;
    private String phoneNumber;

    public InputUserDTO(String name , String lastName , String phoneNumber) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

}



