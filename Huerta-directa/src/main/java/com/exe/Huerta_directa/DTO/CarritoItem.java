package com.exe.Huerta_directa.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItem {
    private Long productId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer cantidad;
    private String imagen;

    // Método auxiliar para calcular el subtotal de este item
    public BigDecimal getSubtotal() {
        return precio.multiply(new BigDecimal(cantidad));
    }
}