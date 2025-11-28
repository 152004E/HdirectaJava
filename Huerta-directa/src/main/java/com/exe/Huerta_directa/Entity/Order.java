package com.exe.Huerta_directa.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estado;   // PENDIENTE - PAGADO
    private Long paymentId;  // ID del pago de Mercado Pago
}

