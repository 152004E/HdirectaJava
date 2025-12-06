package com.exe.Huerta_directa.Strategy.strategies;


import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;



@Component
public class StorePickupStrategy implements DeliveryStrategy {

    @Override
    public String getType() {
        return "store_pickup";
    }

    @Override
    public String calculateDelivery(String region) {
        int preparationTime = getPreparationTime(region);
        String storeLocation = getStoreLocation(region);

        return """
            <html>
            <head>
                <style>
                    body { font-family: Arial; padding: 20px; background: #f5f5f5; }
                    .result { background: #f3e5f5; padding: 20px; border-radius: 8px; border-left: 5px solid #7b1fa2; }
                    h2 { color: #4a148c; }
                    .store { 
                        background: white; 
                        padding: 10px; 
                        margin: 10px 0;
                        border-radius: 4px;
                        border: 1px solid #ce93d8;
                    }
                </style>
            </head>
            <body>
                <div class="result">
                    <h2>🏪 Recogida en Tienda</h2>
                    <p><strong>Región seleccionada:</strong> %s</p>
                    <p><strong>Tiempo de preparación:</strong> %d minutos</p>
                    <div class="store">
                        <strong>📍 Punto de recogida:</strong> %s
                    </div>
                    <p><strong>Costo:</strong> <span style="color: green; font-weight: bold;">GRATIS</span></p>
                    <p><strong>Algoritmo:</strong> Sin costo de envío, solo tiempo de preparación del pedido</p>
                    <p><em>Presenta tu código de pedido al recoger</em></p>
                </div>
            </body>
            </html>
        """.formatted(region, preparationTime, storeLocation);
    }

    private int getPreparationTime(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> 30;
            case "caribe" -> 45;
            case "pacifica" -> 40;
            case "orinoquia" -> 50;
            case "amazonia" -> 60;
            default -> 35;
        };
    }

    private String getStoreLocation(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> "Centro de Acopio Bogotá - Calle 26 #68-34";
            case "caribe" -> "Centro de Acopio Barranquilla - Cra 53 #82-10";
            case "pacifica" -> "Centro de Acopio Cali - Av 3N #34-68";
            case "orinoquia" -> "Centro de Acopio Villavicencio - Cra 40 #15-23";
            case "amazonia" -> "Centro de Acopio Leticia - Calle 8 #9-75";
            default -> "Centro de Acopio Principal - Bogotá";
        };
    }
}