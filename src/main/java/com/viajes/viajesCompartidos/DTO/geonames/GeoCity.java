package com.viajes.viajesCompartidos.DTO.geonames;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoCity implements Serializable {
    private String adminCode1;
    private String lng;
    private int geonameId;
    private String toponymName;
    private String countryId;
    private String fcl;
    private int population;
    private String countryCode;
    private String name;

    // Getters y Setters

    public static class AdminCodes1 {
        private String ISO3166_2;

        public String getISO3166_2() {
            return ISO3166_2;
        }

        public void setISO3166_2(String ISO3166_2) {
            this.ISO3166_2 = ISO3166_2;
        }
    }
}
