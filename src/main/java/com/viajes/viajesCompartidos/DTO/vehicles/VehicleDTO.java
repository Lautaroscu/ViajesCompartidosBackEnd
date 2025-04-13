package com.viajes.viajesCompartidos.DTO.vehicles;

import com.viajes.viajesCompartidos.entities.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO implements Serializable {
    private String plate;
    private String model;
    private String color;
    private int year;
    private String brand;
    private int seatingCapacity;
    private boolean available;
    private boolean predetermined;


    public VehicleDTO(Vehicle vehicle) {
        this.plate = vehicle.getPlate();
        this.model = vehicle.getModel();
        this.color = vehicle.getColor();
        this.year = vehicle.getYear();
        this.brand = vehicle.getBrand();
        this.seatingCapacity = vehicle.getSeatingCapacity();
        this.available = vehicle.isAvailable();
        this.predetermined = vehicle.isPredetermined();

    }
}
