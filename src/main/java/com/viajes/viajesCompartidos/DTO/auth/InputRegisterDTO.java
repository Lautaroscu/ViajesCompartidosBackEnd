package com.viajes.viajesCompartidos.DTO.auth;

import lombok.Getter;

@Getter
public class InputRegisterDTO {
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;

    public InputRegisterDTO(String name, String lastName, String email, String phoneNumber, String password, String confirmPassword) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

}
