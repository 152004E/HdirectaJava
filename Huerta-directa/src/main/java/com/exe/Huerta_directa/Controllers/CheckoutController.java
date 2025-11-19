package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.MercadoPagoDTO;
import com.exe.Huerta_directa.Service.MercadoPagoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "*")
public class CheckoutController {

    private final MercadoPagoService mercadoPagoService;

    @Autowired
    public CheckoutController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping("/preference")
    public ResponseEntity<?> createPreference(@RequestBody MercadoPagoDTO checkoutData) {
        try {
            // ✅ Ahora recibe un Map con init_point y preference_id
            Map<String, String> preferenceData = mercadoPagoService.createPaymentPreference(checkoutData);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "init_point", preferenceData.get("init_point"),
                    "preference_id", preferenceData.get("preference_id")
            ));

        } catch (MPApiException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error en la API de Mercado Pago",
                            "details", e.getApiResponse().getContent()
                    ));

        } catch (MPException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error interno del SDK de Mercado Pago: " + e.getMessage()
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", "error",
                            "message", e.getMessage()
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error inesperado al crear la preferencia de pago: " + e.getMessage()
                    ));
        }
    }
}