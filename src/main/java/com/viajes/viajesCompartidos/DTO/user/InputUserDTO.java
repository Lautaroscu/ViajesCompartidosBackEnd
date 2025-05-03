package com.viajes.viajesCompartidos.DTO.user;

import com.viajes.viajesCompartidos.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class InputUserDTO implements Serializable {
    private String name;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private String residenceCity;

    public InputUserDTO(String name , String lastName , String phoneNumber ,  String email,String password , String residenceCity) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.residenceCity = residenceCity;
    }

}



