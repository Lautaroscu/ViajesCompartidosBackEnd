package com.viajes.viajesCompartidos.DTO.location;

import com.viajes.viajesCompartidos.entities.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutputLocationDTO  extends InputLocationDTO {
    private int id;

        public OutputLocationDTO(Location location) {
            super(location);
            this.id = location.getId();
    }
}
