package com.viajes.viajesCompartidos.DTO.user;

import com.viajes.ViajesCompartidos.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class OutputUserDTO extends InputUserDTO implements Serializable {
    private int id;
    public OutputUserDTO(User user) {
        super(user.getFirstName() , user.getLastName() , user.getPhone());
        this.id = user.getUserId();
    }
}
