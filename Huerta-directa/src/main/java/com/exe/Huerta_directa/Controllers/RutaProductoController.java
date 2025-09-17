package com.exe.Huerta_directa.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RutaProductoController {

    @GetMapping("/agregar_producto")
    public String mostrarFormulario() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "Agreagar_producto/Agregar_producto";
    }
}