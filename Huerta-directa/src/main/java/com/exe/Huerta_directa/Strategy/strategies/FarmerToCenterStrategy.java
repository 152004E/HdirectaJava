package com.exe.Huerta_directa.Strategy.strategies;



import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;

@Component
public class FarmerToCenterStrategy implements DeliveryStrategy {

    @Override
    public String getType() {
        return "farmer_to_center";
    }

    @Override
    public String calculateDelivery(String region) {
        int timeInMinutes = getTimeByRegion(region);

        return """
            <html>
            <head>
                <style>
                    body { font-family: Arial; padding: 20px; }
                    .result { background: #e8f5e9; padding: 15px; border-radius: 8px; }
                </style>
            </head>
            <body>
                <div class="result">
                    <h2>🚜 Transporte Campesino → Centro de Acopio</h2>
                    <p><strong>Región:</strong> %s</p>
                    <p><strong>Tiempo estimado:</strong> %d minutos</p>
                    <p><strong>Algoritmo:</strong> Transporte rural + tiempo de carga</p>
                </div>
            </body>
            </html>
        """.formatted(region, timeInMinutes);
    }

    private int getTimeByRegion(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> 90;
            case "caribe" -> 210;
            case "pacifica" -> 180;
            case "orinoquia" -> 270;
            case "amazonia" -> 360;
            default -> 120;
        };
    }
}