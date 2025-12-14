package com.exe.Huerta_directa.Strategy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryResult {

    private boolean canDeliver;
    private String vehicleName;
    private String localidad;
    private int estimatedTime;
    private double percentage;
    private String formattedTime;
    private String restrictions;
}
