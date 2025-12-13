// 3. TRUCK STRATEGY
// ============================================
package com.exe.Huerta_directa.Strategy.strategies;

import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import com.exe.Huerta_directa.Strategy.DeliveryResult;
import com.exe.Huerta_directa.Strategy.Localidad;
import org.springframework.stereotype.Component;

@Component
public class TruckDeliveryStrategy implements DeliveryStrategy {

    private static final int BASE_TIME = 30;

    @Override
    public String getType() {
        return "truck";
    }

    @Override
    public DeliveryResult calculate(Localidad localidad) {
        double factor = localidad.getFactor();

        // El camión es más lento en ciudad, más rápido en carretera
        double vehicleFactor = calculateVehicleFactor(factor);
        double finalFactor = factor * vehicleFactor;
        int estimatedTime = (int) Math.round(BASE_TIME * finalFactor);

        // Restricciones según zona
        String restrictions = null;
        if (factor < 1.20) {
            restrictions = "Zona céntrica: Posibles restricciones de circulación en horas pico.";
        }

        return new DeliveryResult(
                true,
                "Camión",
                localidad.name(),
                estimatedTime,
                finalFactor * 100,
                estimatedTime + " minutos",
                restrictions
        );
    }

    private double calculateVehicleFactor(double localityFactor) {
        if (localityFactor < 1.30) {
            return 1.15; // 15% más lento en ciudad
        } else if (localityFactor >= 1.60) {
            return 0.95; // 5% más rápido en zonas lejanas (carretera)
        }
        return 1.05; // 5% más lento estándar
    }
}
