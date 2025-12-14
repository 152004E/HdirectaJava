package com.exe.Huerta_directa.Impl;
import com.exe.Huerta_directa.Service.DeliveryService;
import org.springframework.stereotype.Service;
import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import com.exe.Huerta_directa.Service.DeliveryStrategyFactory;
import com.exe.Huerta_directa.Strategy.Localidad;
import com.exe.Huerta_directa.Strategy.DeliveryResult;

@Service

public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryStrategyFactory factory;

    public DeliveryServiceImpl(DeliveryStrategyFactory factory) {
        this.factory = factory;
    }

    @Override
    public DeliveryResult processDelivery(String type, String localidadStr) {

        DeliveryStrategy strategy = factory.getStrategy(type);

        Localidad localidad = Localidad.valueOf(localidadStr.toUpperCase());

        return strategy.calculate(localidad);
    }
}
