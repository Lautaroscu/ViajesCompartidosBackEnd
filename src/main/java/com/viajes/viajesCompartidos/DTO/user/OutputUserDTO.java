package com.viajes.viajesCompartidos.DTO.user;

import com.viajes.viajesCompartidos.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class OutputUserDTO implements Serializable {
    private int id;
    private String name;
    private String email;
    private String lastName;
    private String phoneNumber;

    public OutputUserDTO(User user) {
        this.id = user.getUserId();
        this.name = user.getFirstName();
        this.email = user.getEmail();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhone();
    }

}
