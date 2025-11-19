package com.exe.Huerta_directa.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import com.mercadopago.MercadoPagoConfig;

@Configuration
public class MercadoPagoConfigClass {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @PostConstruct
    public void initializeMercadoPagoSDK() {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);
            System.out.println("✅ Mercado Pago SDK inicializado correctamente.");
        } catch (Exception e) {
            System.err.println("❌ Error inicializando Mercado Pago SDK: " + e.getMessage());
        }
    }
}

