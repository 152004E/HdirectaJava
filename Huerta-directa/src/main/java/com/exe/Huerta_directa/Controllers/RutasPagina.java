package com.exe.Huerta_directa.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RutasPagina {

    @GetMapping({"/", "/index"})
    public String info() {
        return "index"; // busca templates/index.html
    }
    @GetMapping("/agregar_producto")
    public String mostrarFormulario() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "Agreagar_producto/Agregar_producto";
    }
    @GetMapping("/LogIn")
    public String mostrarLogin() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "login/LogIn";
    }
    @GetMapping("/Dashboardd")
    public String mostrarDashBoard() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "DashBoard/DashBoardd";
    }


}
