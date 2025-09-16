package com.exe.Huerta_directa.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RutaProductoController {

    @GetMapping("/agregar_producto")
    public String mostrarFormulario() {
        // Spring va a buscar: src/main/resources/templates/Agregar_producto/index.html
        return "Agregar_producto/index";
    }
}