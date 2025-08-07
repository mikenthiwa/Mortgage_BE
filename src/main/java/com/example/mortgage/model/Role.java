package com.example.mortgage.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

    public enum RoleName {
        USER,
        ADMIN
    }

    //Get name
    public RoleName getName() {
        return name;
    }
}
