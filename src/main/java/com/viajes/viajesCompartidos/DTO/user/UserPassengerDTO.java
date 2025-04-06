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
    private String firstName;
    private String lastName;
    private BigDecimal valoration;
    private LocalDate registeredAt;

    public UserPassengerDTO(User user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.valoration = user.getValoration();
    }
}
