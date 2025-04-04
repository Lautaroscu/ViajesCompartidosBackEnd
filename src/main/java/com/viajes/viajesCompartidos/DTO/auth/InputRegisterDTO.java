package com.viajes.viajesCompartidos.DTO.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor

public class InputRegisterDTO implements Serializable {
    private String name;
    private String lastName;
    private String email;
    private String residenceCity;
    private String phoneNumber;
    private String password;
    private String confirmPassword;

    public InputRegisterDTO(String name, String lastName, String email,String residenceCity , String phoneNumber, String password, String confirmPassword) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.residenceCity = residenceCity;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

}
