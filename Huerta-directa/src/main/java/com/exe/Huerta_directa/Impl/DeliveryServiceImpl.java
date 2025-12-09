package com.exe.Huerta_directa.Impl;
import com.exe.Huerta_directa.Service.DeliveryService;
import org.springframework.stereotype.Service;
import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import com.exe.Huerta_directa.Service.DeliveryStrategyFactory;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryStrategyFactory factory;

    public DeliveryServiceImpl(DeliveryStrategyFactory factory) {
        this.factory = factory;
    }

    @Override
    public String processDelivery(String type, String region) {
        DeliveryStrategy strategy = factory.getStrategy(type);

        if (strategy == null) {
            return "<h2>Tipo de entrega no válido</h2>";
        }

        return strategy.calculateDelivery(region);
    }
}