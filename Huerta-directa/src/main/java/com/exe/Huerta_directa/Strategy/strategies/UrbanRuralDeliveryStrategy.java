package com.exe.Huerta_directa.Strategy.strategies;



import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;

@Component
public class UrbanRuralDeliveryStrategy implements DeliveryStrategy {

    @Override
    public String getType() {
        return "urban_rural";
    }

    @Override
    public String calculateDelivery(String region) {
        int timeInMinutes = getTimeByRegion(region);
        int cost = calculateCost(region);
        String difficulty = getDifficulty(region);

        return """
            <html>
            <head>
                <style>
                    body { font-family: Arial; padding: 20px; background: #f5f5f5; }
                    .result { background: #e1f5fe; padding: 20px; border-radius: 8px; border-left: 5px solid #0277bd; }
                    h2 { color: #01579b; }
                    .difficulty { 
                        display: inline-block; 
                        padding: 5px 10px; 
                        background: #fff59d; 
                        border-radius: 4px;
                        font-weight: bold;
                    }
                </style>
            </head>
            <body>
                <div class="result">
                    <h2>🚗 Entrega Urbano-Rural</h2>
                    <p><strong>Región seleccionada:</strong> %s</p>
                    <p><strong>Tiempo estimado:</strong> %d minutos</p>
                    <p><strong>Costo estimado:</strong> $%,d COP</p>
                    <p><strong>Dificultad del terreno:</strong> <span class="difficulty">%s</span></p>
                    <p><strong>Algoritmo:</strong> Cálculo mixto considerando vías urbanas y rurales</p>
                    <p><em>Incluye tiempo de navegación por vías secundarias</em></p>
                </div>
            </body>
            </html>
        """.formatted(region, timeInMinutes, cost, difficulty);
    }

    private int getTimeByRegion(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> 75;
            case "caribe" -> 150;
            case "pacifica" -> 180;
            case "orinoquia" -> 210;
            case "amazonia" -> 270;
            default -> 90;
        };
    }

    private int calculateCost(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> 18000;
            case "caribe" -> 42000;
            case "pacifica" -> 48000;
            case "orinoquia" -> 58000;
            case "amazonia" -> 75000;
            default -> 25000;
        };
    }

    private String getDifficulty(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> "Media";
            case "caribe" -> "Baja";
            case "pacifica" -> "Alta";
            case "orinoquia" -> "Alta";
            case "amazonia" -> "Muy Alta";
            default -> "Media";
        };
    }
}
