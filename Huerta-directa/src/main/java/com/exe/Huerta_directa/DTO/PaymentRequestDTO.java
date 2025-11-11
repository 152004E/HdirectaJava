package com.exe.Huerta_directa.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {
    private String titulo;
    private int cantidad;
    private double precioUnitario;
    private String nombreComprador;
    private String correoComprador;
}
