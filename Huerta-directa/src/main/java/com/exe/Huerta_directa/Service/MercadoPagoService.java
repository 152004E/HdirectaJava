package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.DTO.ItemDTO;
import com.exe.Huerta_directa.DTO.MercadoPagoDTO;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access.token}")
    private String mercadoPagoAccessToken;

    @Value("${app.base.url=http://localhost:8085}")
    private String baseUrl;

    public Map<String, String> createPaymentPreference(MercadoPagoDTO checkoutData)
            throws MPException, MPApiException {

        // 🔹 Configurar token
        MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

        // 🔹 Crear ítems
        List<PreferenceItemRequest> items = checkoutData.getItems().stream()
                .map(item -> PreferenceItemRequest.builder()
                        .title(item.getTitle())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .currencyId("COP")
                        .build())
                .collect(Collectors.toList());

        // 🔹 Crear preferencia con backUrls correctamente
        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .externalReference(String.valueOf(checkoutData.getOrderId()))
                .backUrls(
                        com.mercadopago.client.preference.PreferenceBackUrlsRequest.builder()
                                .success(baseUrl + "/checkout/success")
                                .failure(baseUrl + "/checkout/failure")
                                .pending(baseUrl + "/checkout/pending")
                                .build()
                )
                .autoReturn("approved")
                .build();

        // 🔹 Crear preferencia en Mercado Pago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        // 🔹 Devolver datos
        return Map.of(
                "init_point", preference.getInitPoint(),
                "preference_id", preference.getId()
        );
    }
}
