package com.exe.Huerta_directa.Strategy.strategies;

import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import com.exe.Huerta_directa.Strategy.DeliveryResult;
import com.exe.Huerta_directa.Strategy.Localidad;
import org.springframework.stereotype.Component;

@Component
public class MotorcycleDeliveryStrategy implements DeliveryStrategy {

    private static final int BASE_TIME = 30; // Chapinero base

    @Override
    public String getType() {
        return "motorcycle";
    }

    @Override
    public DeliveryResult calculate(Localidad localidad) {
        double factor = localidad.getFactor();

        // Validación: La moto no puede ir a zonas muy lejanas
        if (factor > 1.75) {
            return new DeliveryResult(
                    false,
                    "Motocicleta",
                    localidad.name(),
                    0,
                    0.0,
                    "N/A",
                    "La motocicleta no puede realizar entregas a localidades muy lejanas por seguridad y autonomía limitada."
            );
        }

        // Cálculo del tiempo con ajuste de vehículo
        double vehicleFactor = calculateVehicleFactor(factor);
        double finalFactor = factor * vehicleFactor;
        int estimatedTime = (int) Math.round(BASE_TIME * finalFactor);

        // Restricciones opcionales
        String restrictions = null;
        if (factor >= 1.50) {
            restrictions = "Zona lejana: El tiempo puede variar por condiciones de tráfico.";
        }

        return new DeliveryResult(
                true,
                "Motocicleta",
                localidad.name(),
                estimatedTime,
                finalFactor * 100,
                estimatedTime + " minutos",
                restrictions
        );
    }

    private double calculateVehicleFactor(double localityFactor) {
        // La moto es más rápida en zonas cercanas
        if (localityFactor < 1.30) {
            return 0.85; // 15% más rápido
        } else if (localityFactor < 1.60) {
            return 0.95; // 5% más rápido
        }
        return 1.00; // Normal en zonas lejanas
    }
}
