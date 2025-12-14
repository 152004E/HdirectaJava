package com.exe.Huerta_directa.Service;
import com.exe.Huerta_directa.Strategy.DeliveryResult;

public interface DeliveryService {
    DeliveryResult processDelivery(String type, String localidad);
}

