package com.exe.Huerta_directa.Strategy.strategies;



import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;

@Component
public class AlliedTransportStrategy implements DeliveryStrategy {

    @Override
    public String getType() {
        return "allied_transport";
    }

    @Override
    public String calculateDelivery(String region) {
        int timeInMinutes = getTimeByRegion(region);
        int cost = calculateCost(region);

        return """
            <html>
            <head>
                <style>
                    body { font-family: Arial; padding: 20px; background: #f5f5f5; }
                    .result { background: #fff3e0; padding: 20px; border-radius: 8px; border-left: 5px solid #ff9800; }
                    h2 { color: #e65100; }
                </style>
            </head>
            <body>
                <div class="result">
                    <h2>🚛 Transporte por Aliado Logístico</h2>
                    <p><strong>Región seleccionada:</strong> %s</p>
                    <p><strong>Tiempo estimado:</strong> %d minutos</p>
                    <p><strong>Costo estimado:</strong> $%,d COP</p>
                    <p><strong>Algoritmo:</strong> Transporte tercerizado con red de distribución nacional</p>
                    <p><em>Incluye seguro y seguimiento GPS</em></p>
                </div>
            </body>
            </html>
        """.formatted(region, timeInMinutes, cost);
    }

    private int getTimeByRegion(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> 120;
            case "caribe" -> 360;
            case "pacifica" -> 300;
            case "orinoquia" -> 480;
            case "amazonia" -> 600;
            default -> 180;
        };
    }

    private int calculateCost(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> 25000;
            case "caribe" -> 65000;
            case "pacifica" -> 55000;
            case "orinoquia" -> 85000;
            case "amazonia" -> 120000;
            default -> 35000;
        };
    }
}