package com.viajes.viajesCompartidos.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.viajes.viajesCompartidos.entities.payments.Recharge;
import com.viajes.viajesCompartidos.entities.payments.Wallet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_entity")
@Getter
@Setter
@ToString
public class User {
    private final BigDecimal INITIAL_USER_SCORE = BigDecimal.valueOf(3.00);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String password;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Wallet wallet;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Recharge> recharges = new ArrayList<>();
    @Column
    private LocalDate registeredAt;

    @OneToMany(mappedBy = "owner" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Vehicle> vehicles = new ArrayList<>();

    @Column(name = "valoration", precision = 3, scale = 2, nullable = false)
    private BigDecimal valoration;
    @Column
    private String residenceCity;

    @Column
    private boolean verifiedEmail;

    @Column
    private boolean verifiedPhone;

    @Column
    private String userImage;

    public User() {
        ZoneId zoneId =ZoneId.of("America/Argentina/Buenos_Aires");
        registeredAt = LocalDate.now(zoneId);
        vehicles = new ArrayList<>();
        valoration = INITIAL_USER_SCORE;
        wallet = new Wallet(this);
        this.verifiedEmail = false;
        this.verifiedPhone = false;

    }

    public User(String firstName, String lastName, String phone , String email, String password  , String residenceCity ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.registeredAt = LocalDate.now();
        this.valoration = BigDecimal.ZERO;
        this.residenceCity = residenceCity;
        this.wallet = new Wallet(this);
    }
    public void addRecharge(Recharge recharge) {
        recharges.add(recharge);
    }
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }
  public boolean deleteVehicle(Vehicle vehicle) {
        return vehicles.remove(vehicle);
  }
    public boolean setVehiclePredetermined(String plate) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isPredetermined()) {
                vehicle.setPredetermined(false);
            }
            if (vehicle.getPlate().equals(plate)) {
                vehicle.setPredetermined(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        if (user == null) return false;

        return user.getUserId() == this.userId && user.getFirstName().equals(this.firstName) && user.getLastName().equals(this.lastName);


    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
        if(wallet != null) {
            wallet.setUser(this);
        }
    }
}
