package com.viajes.viajesCompartidos.DTO.location;

import com.viajes.viajesCompartidos.entities.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputLocationDTO implements Serializable {
    private  String cityName;
    private double cityLatitude;
    private double cityLongitude;
    private String exactPlace;

    public InputLocationDTO(String cityName , double cityLatitude, double cityLongitude) {
        this.cityName = cityName;
        this.cityLatitude = cityLatitude;
        this.cityLongitude = cityLongitude;
    }
    public InputLocationDTO(Location location) {
            this.cityName = location.getCity();
            this.cityLatitude = location.getLatitude();
            this.cityLongitude = location.getLongitude();
            this.exactPlace = location.getExactPlace();
    }

}
