package com.exe.Huerta_directa.Strategy;

public interface DeliveryStrategy {
    String getType();
    DeliveryResult calculate(Localidad localidad);
}


