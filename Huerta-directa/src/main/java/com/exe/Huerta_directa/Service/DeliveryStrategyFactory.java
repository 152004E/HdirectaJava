package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.Strategy.DeliveryStrategy;



public interface DeliveryStrategyFactory {
    DeliveryStrategy getStrategy(String type);
}
