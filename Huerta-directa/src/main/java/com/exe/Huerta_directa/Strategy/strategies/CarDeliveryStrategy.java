package com.exe.Huerta_directa.Strategy.strategies;

import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import com.exe.Huerta_directa.Strategy.DeliveryResult;
import com.exe.Huerta_directa.Strategy.Localidad;
import org.springframework.stereotype.Component;

@Component
public class CarDeliveryStrategy implements DeliveryStrategy {

    private static final int BASE_TIME = 30;

    @Override
    public String getType() {
        return "car";
    }

    @Override
    public DeliveryResult calculate(Localidad localidad) {
        double factor = localidad.getFactor();

        // El carro puede ir a todas las localidades
        // Factor de vehículo estándar
        double vehicleFactor = 1.00;
        double finalFactor = factor * vehicleFactor;
        int estimatedTime = (int) Math.round(BASE_TIME * finalFactor);

        // Restricciones para zonas muy alejadas
        String restrictions = null;
        if (factor >= 2.00) {
            restrictions = "Zona muy alejada: Se recomienda coordinar la entrega con anticipación.";
        }

        return new DeliveryResult(
                true,
                "Carro",
                localidad.name(),
                estimatedTime,
                finalFactor * 100,
                estimatedTime + " minutos",
                restrictions
        );
    }
}