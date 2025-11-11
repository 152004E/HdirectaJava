package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.DTO.PaymentRequestDTO;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

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
}
