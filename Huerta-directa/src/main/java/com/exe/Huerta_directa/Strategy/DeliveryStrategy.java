package com.exe.Huerta_directa.Strategy;


public interface DeliveryStrategy {
    String getType();
    String calculateDelivery(String region); // Recibe la región de Colombia
}

