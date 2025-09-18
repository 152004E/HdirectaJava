package com.exe.Huerta_directa.Entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;


@Entity
@Table (name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column (name = "name", nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String name;

    @Column (name = "email", nullable = false, length = 250, unique = true)
    @NotBlank
    @Size(max = 250)
    private String email;

    @Column (name = "password", nullable = false, length = 250)
    @NotBlank
    @Size(max = 250)
    private String password;

    @ManyToOne
    @JoinColumn (name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products;

}
