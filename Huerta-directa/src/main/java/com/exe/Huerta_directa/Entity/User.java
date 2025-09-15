package com.exe.Huerta_directa.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table (name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column (name = "name", nullable = false, length = 100)
    private String name;

    @Column (name = "email", nullable = false, length = 250, unique = true)
    private String email;

    @Column (name = "password", nullable = false, length = 250)
    private String password;

    @ManyToOne
    @JoinColumn (name = "role_id", nullable = false)
    private Role role;

}
