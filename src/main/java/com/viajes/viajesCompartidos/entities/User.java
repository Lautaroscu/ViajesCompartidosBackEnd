package com.viajes.viajesCompartidos.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
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

    public User() {
    }

    public User(String firstName, String lastName, String phone , String email, String password ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.balance = BigDecimal.ZERO;
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
