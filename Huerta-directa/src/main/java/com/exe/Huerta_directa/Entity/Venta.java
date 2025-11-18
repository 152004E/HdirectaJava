package com.exe.Huerta_directa.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "ventas")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "Amount", nullable = false)

    private Double Amount;

    @Column(name = "Stock", nullable = false)

    private Integer stock;

    @Column(name= "Discount", nullable = false)
    private Double discount;

    @Column(name = "Subtotal", nullable = false)
    private Double subtotal;


}
