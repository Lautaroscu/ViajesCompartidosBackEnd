package com.viajes.viajesCompartidos.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.viajes.viajesCompartidos.entities.payments.Recharge;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_entity")
@Getter
@Setter
@ToString
public class User {
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
    @Column
    private BigDecimal balance;
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


    public User() {
        registeredAt = LocalDate.now();
        vehicles = new ArrayList<>();
        valoration = BigDecimal.ZERO;
        balance = BigDecimal.ZERO;

    }

    public User(String firstName, String lastName, String phone , String email, String password  , String residenceCity) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.balance = BigDecimal.ZERO;
        this.registeredAt = LocalDate.now();
        this.valoration = BigDecimal.ZERO;
        this.residenceCity = residenceCity;
    }
    public void addRecharge(Recharge recharge) {
        recharges.add(recharge);
    }
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        if (user == null) return false;

        return user.getUserId() == this.userId && user.getFirstName().equals(this.firstName) && user.getLastName().equals(this.lastName);


    }

    public void setBalance(BigDecimal balance) {
        this.balance = BigDecimal.valueOf(balance.doubleValue());
    }
}
