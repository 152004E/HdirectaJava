package com.exe.Huerta_directa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.exe.Huerta_directa.Service.MercadoPagoService;
import com.exe.Huerta_directa.DTO.PaymentRequestDTO;

@RestController
@RequestMapping("/api/pagos")
public class PaymentController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping("/crear")
    public String crearPago(@RequestBody PaymentRequestDTO request) {
        try {
            return mercadoPagoService.createPreference(request);
        } catch (Exception e) {
            return "Error al crear la preferencia: " + e.getMessage();
        }
    }
}
