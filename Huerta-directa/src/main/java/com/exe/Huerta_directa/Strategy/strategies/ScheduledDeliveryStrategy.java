package com.exe.Huerta_directa.Strategy.strategies;


import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;

@Component
public class ScheduledDeliveryStrategy implements DeliveryStrategy {

    @Override
    public String getType() {
        return "scheduled";
    }

    @Override
    public String calculateDelivery(String region) {
        int daysToDeliver = getDaysToDeliver(region);
        int cost = calculateCost(region);
        String scheduleOptions = getScheduleOptions(region);

        return """
            <html>
            <head>
                <style>
                    body { font-family: Arial; padding: 20px; background: #f5f5f5; }
                    .result { background: #e8eaf6; padding: 20px; border-radius: 8px; border-left: 5px solid #3f51b5; }
                    h2 { color: #1a237e; }
                    .schedule { 
                        background: white; 
                        padding: 15px; 
                        margin: 10px 0;
                        border-radius: 4px;
                        border: 1px solid #9fa8da;
                    }
                    .days {
                        font-size: 24px;
                        font-weight: bold;
                        color: #3f51b5;
                    }
                </style>
            </head>
            <body>
                <div class="result">
                    <h2>📅 Entrega Programada</h2>
                    <p><strong>Región seleccionada:</strong> %s</p>
                    <p><strong>Tiempo de entrega:</strong> <span class="days">%d días hábiles</span></p>
                    <p><strong>Costo con descuento:</strong> $%,d COP</p>
                    <div class="schedule">
                        <strong>🕐 Horarios disponibles:</strong><br>
                        %s
                    </div>
                    <p><strong>Algoritmo:</strong> Optimización de rutas con ventana de tiempo flexible</p>
                    <p><em>⭐ Ahorra 15%% programando tu entrega con anticipación</em></p>
                </div>
            </body>
            </html>
        """.formatted(region, daysToDeliver, cost, scheduleOptions);
    }

    private int getDaysToDeliver(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> 2;
            case "caribe" -> 4;
            case "pacifica" -> 3;
            case "orinoquia" -> 5;
            case "amazonia" -> 7;
            default -> 3;
        };
    }

    private int calculateCost(String region) {
        // 15% de descuento sobre el precio normal
        return switch (region.toLowerCase()) {
            case "andina" -> 15300;
            case "caribe" -> 38250;
            case "pacifica" -> 35700;
            case "orinoquia" -> 42500;
            case "amazonia" -> 63750;
            default -> 21250;
        };
    }

    private String getScheduleOptions(String region) {
        return switch (region.toLowerCase()) {
            case "andina" -> "• Lunes a Viernes: 8am-12pm o 2pm-6pm<br>• Sábados: 8am-12pm";
            case "caribe" -> "• Martes y Jueves: 9am-1pm o 3pm-7pm<br>• Sábados: 9am-1pm";
            case "pacifica" -> "• Lunes, Miércoles, Viernes: 8am-12pm o 2pm-6pm";
            case "orinoquia" -> "• Miércoles y Viernes: 10am-2pm o 3pm-7pm";
            case "amazonia" -> "• Viernes: 9am-1pm (semanal)";
            default -> "• Días hábiles: 8am-12pm o 2pm-6pm";
        };
    }
}
