package com.viajes.viajesCompartidos.DTO.user;

import com.viajes.viajesCompartidos.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPassengerDTO implements Serializable {

    private int id;
    private String firstName;
    private String lastName;
    private BigDecimal valoration;
    private LocalDate registeredAt;
    private String phoneNumber;

    public UserPassengerDTO(User user){
        this.id = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.valoration = user.getValoration();
        this.registeredAt = user.getRegisteredAt();
        this.phoneNumber = user.getPhone();
    }
}
