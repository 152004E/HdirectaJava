/*package com.exe.Huerta_directa.config;

import com.mercadopago.MercadoPagoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class MercadoPagoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MercadoPagoConfiguration.class);

    @Value("${mercadopago.access.token}")
    private String accessToken;

    // Opcional: timeouts si la SDK lo soporta (consultar docs). Comentado por compatibilidad.
    // @Value("${mercadopago.connection.timeout:5000}")
    // private int connectionTimeout;

    @PostConstruct
    public void init() {
        if (accessToken == null || accessToken.isBlank()) {
            log.warn("MercadoPago access token no está configurado. Agrega mercadopago.access.token en application.properties");
            return;
        }
        MercadoPagoConfig.setAccessToken(accessToken);
        log.info("MercadoPago access token configurado correctamente.");

        // Si la SDK soporta setConnectionRequestTimeout / setSocketTimeout, configúralos aquí.
        // Ejemplo (COMENTADO): MercadoPagoConfig.setConnectionRequestTimeout(connectionTimeout);
    }
}
*/


