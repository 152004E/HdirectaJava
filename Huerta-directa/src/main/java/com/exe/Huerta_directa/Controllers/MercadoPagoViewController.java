package com.exe.Huerta_directa.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/MercadoPago")
public  class MercadoPagoViewController {

    @GetMapping("/success")
    public String success() {
        return "MercadoPago/success";
    }

    @GetMapping("/pending")
    public String pending() {
        return "MercadoPago/pending";
    }

    @GetMapping("/failure")
    public String failure() {
        return "MercadoPago/failure";
    }


    //Controller vistas Modulo pagos

    @GetMapping("/modulo-pagos")
    public String moduloPagos() {
        return "MercadoPago/Resumen_Pago";
    }
}