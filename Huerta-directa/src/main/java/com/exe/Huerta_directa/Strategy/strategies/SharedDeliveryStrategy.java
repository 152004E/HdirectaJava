
package com.exe.Huerta_directa.Strategy.strategies;

import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import com.exe.Huerta_directa.Strategy.DeliveryResult;
import com.exe.Huerta_directa.Strategy.Localidad;
import org.springframework.stereotype.Component;

@Component
public class SharedDeliveryStrategy implements DeliveryStrategy {

    private static final int BASE_TIME = 30;

    @Override
    public String getType() {
        return "shared";
    }

    @Override
    public DeliveryResult calculate(Localidad localidad) {
        double factor = localidad.getFactor();

        // El delivery compartido siempre es más lento por las paradas
        double vehicleFactor = calculateVehicleFactor(factor);
        double finalFactor = factor * vehicleFactor;
        int estimatedTime = (int) Math.round(BASE_TIME * finalFactor);

        // Siempre tiene restricción informativa
        String restrictions = "Entrega compartida: El tiempo puede extenderse debido a múltiples paradas. Económico y eco-friendly.";

        return new DeliveryResult(
                true,
                "Delivery Compartido",
                localidad.name(),
                estimatedTime,
                finalFactor * 100,
                estimatedTime + " minutos",
                restrictions
        );
    }

    private double calculateVehicleFactor(double localityFactor) {
        // Siempre más lento por las paradas múltiples
        if (localityFactor < 1.30) {
            return 1.40; // 40% más lento en ciudad (muchas paradas)
        } else if (localityFactor >= 1.60) {
            return 1.25; // 25% más lento en zonas lejanas
        }
        return 1.30; // 30% más lento estándar
    }
}