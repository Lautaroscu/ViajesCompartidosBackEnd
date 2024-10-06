package com.viajes.ViajesCompartidos.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_entity")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String firstName;
    private String lastName;
    private String phone;

    public User() {
    }

    public User(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        if (user == null) return false;

        return user.getUserId() == this.userId && user.getFirstName().equals(this.firstName) && user.getLastName().equals(this.lastName);


    }
}
