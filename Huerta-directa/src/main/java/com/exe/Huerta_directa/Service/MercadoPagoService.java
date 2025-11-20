package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.DTO.ItemDTO;
import com.exe.Huerta_directa.DTO.MercadoPagoDTO;
import com.exe.Huerta_directa.DTO.PaymentRequestDTO;
import com.mercadopago.MercadoPagoConfig;
//import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
//import java.util.ArrayList;
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

/*
    //Santi metodo
    @Value("${mercadopago.access_token}") // ⭐ Lee del properties
    private String accessToken;

    @Value("${mercadopago.success_url}")
    private String successUrl;

    @Value("${mercadopago.failure_url}")
    private String failureUrl;

    @Value("${mercadopago.pending_url}")
    private String pendingUrl;

    public Preference createPreference(PaymentRequestDTO paymentRequest, Long userId)
            throws MPException, MPApiException {

        MercadoPagoConfig.setAccessToken(accessToken);

        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title(paymentRequest.getTitle())
                .quantity(paymentRequest.getQuantity())
                .currencyId("COP")
                .unitPrice(new BigDecimal("5000")) // Precio fijo para probar
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(List.of(item))
                .build();

        PreferenceClient client = new PreferenceClient();
        return client.create(preferenceRequest);
    }

    // }catch(

    // MPApiException e)
    // {
    // System.err.println(" MPApiException capturada:");
    // System.err.println(" - Status: " + e.getStatusCode());
    // System.err.println(" - Message: " + e.getMessage());
    //

    // try {
    // com.mercadopago.net.MPResponse response = e.getApiResponse();
    // System.err.println(" - Response Content: " + response.getContent());
    // System.err.println(" - Response Headers: " + response.getHeaders());
    // } catch (Exception ex) {
    // System.err.println(" - No se pudo extraer el contenido de la respuesta");
    // }

    // throw e;
    */

}

