package com.exe.Huerta_directa.Strategy.strategies;


import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;

@Component
public class MotorcycleDeliveryStrategy implements DeliveryStrategy {

    @Override
    public String getType() {
        return "motorcycle";
    }

    @Override
    public String calculateDelivery(String region) {
        int timeInMinutes = getTimeByRegion(region);

        return """
            <html>
            <head>
                <style>
                    body { font-family: Arial; padding: 20px; }
                    .result { background: #f0f0f0; padding: 15px; border-radius: 8px; }
                </style>
            </head>
            <body>
                <div class="result">
                    <h2>🏍️ Entrega en Motocicleta</h2>
                    <p><strong>Región seleccionada:</strong> %s</p>
                    <p><strong>Tiempo estimado:</strong> %d minutos</p>
                    <p><strong>Algoritmo:</strong> Tiempo base para moto según distancia de Bogotá</p>
                </div>
            </body>
            </html>
        """.formatted(region, timeInMinutes);
    }

    private int getTimeByRegion(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> 45;
            case "caribe" -> 180;
            case "pacifica" -> 150;
            case "orinoquia" -> 240;
            case "amazonia" -> 300;
            default -> 60;
        };
    }
}