package com.rateme.rateme.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    @Column(nullable = false,unique = true) //same username not allowed
    private String username;

    @Column(nullable = false)
    private String email;
    private byte[] passwordHash;
    private byte[] passwordSalt;


}
