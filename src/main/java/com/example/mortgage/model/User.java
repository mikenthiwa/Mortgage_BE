package com.example.mortgage.model;


import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Get roles
    public Set<Role> getRoles() {
        return roles;
    }

    // Get Username
    public String getUsername() {
        return username;
    }

    // Get Password
    public String getPassword() {
        return password;
    }

}
