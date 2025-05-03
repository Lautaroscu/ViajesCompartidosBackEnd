package com.viajes.viajesCompartidos.DTO.user;

import com.viajes.viajesCompartidos.DTO.vehicles.VehicleDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.Vehicle;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OutputUserDTO implements Serializable {
    private int id;
    private String firstName;
    private String email;
    private String residenceCity;
    private LocalDate registeredAt;
    private String lastName;
    private String phoneNumber;
    private Long walletId;
    private BigDecimal valoration;
    private VehicleDTO vehiclePredetermined;
    private String password;


    public OutputUserDTO(User user) {
        this.id = user.getUserId();
        this.firstName = user.getFirstName();
        this.email = user.getEmail();
        this.residenceCity = user.getResidenceCity();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhone();
        this.walletId = user.getWallet().getId();
        this.valoration = user.getValoration();
                            this.vehiclePredetermined = user.getVehicles().stream().filter(Vehicle::isPredetermined).map(VehicleDTO::new).findFirst().orElse(null);
        this.registeredAt = user.getRegisteredAt();
        this.password = user.getPassword();
    }

}
