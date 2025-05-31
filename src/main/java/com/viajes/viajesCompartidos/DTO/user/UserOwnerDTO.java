package com.viajes.viajesCompartidos.DTO.user;

import com.viajes.viajesCompartidos.DTO.vehicles.VehicleDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserOwnerDTO extends UserPassengerDTO {
    private VehicleDTO vehiclePredetermined;

    public UserOwnerDTO(User user) {
        super(user);
        this.vehiclePredetermined = user.getVehicles().stream().filter(Vehicle::isPredetermined).map(VehicleDTO::new).findFirst().orElse(null);
    }
}
