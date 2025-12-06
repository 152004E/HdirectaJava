package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.Service.DeliveryStrategyFactory;
import com.exe.Huerta_directa.Strategy.DeliveryStrategy;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeliveryStrategyFactoryImpl implements DeliveryStrategyFactory {

    private final Map<String, DeliveryStrategy> strategies;

    public DeliveryStrategyFactoryImpl(List<DeliveryStrategy> strategiesList) {
        this.strategies = strategiesList.stream()
                .collect(Collectors.toMap(DeliveryStrategy::getType, s -> s));
    }

    @Override
    public DeliveryStrategy getStrategy(String type) {
        return strategies.get(type);
    }
}
