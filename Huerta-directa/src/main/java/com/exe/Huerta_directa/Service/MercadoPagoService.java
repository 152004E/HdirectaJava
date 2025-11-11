package com.exe.Huerta_directa.Service;

import org.springframework.stereotype.Service;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import java.math.BigDecimal;
import java.util.List;

import com.exe.Huerta_directa.DTO.PaymentRequestDTO;

@Service
public class MercadoPagoService {

    public String createPreference(PaymentRequestDTO request) throws Exception {
        // Configurar el token de Mercado Pago
        MercadoPagoConfig.setAccessToken("TU_ACCESS_TOKEN_AQUI");

        // Cliente de preferencias
        PreferenceClient client = new PreferenceClient();

        // Crear ítem
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title(request.getTitulo())
                .quantity(request.getCantidad())
                .unitPrice(new BigDecimal(request.getPrecioUnitario()))
                .currencyId("COP")
                .build();

        // Crear comprador
        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                .name(request.getNombreComprador())
                .email(request.getCorreoComprador())
                .build();

        // Crear preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(List.of(item))
                .payer(payer)
                .build();

        Preference preference = client.create(preferenceRequest);

        return preference.getInitPoint();
    }
}
