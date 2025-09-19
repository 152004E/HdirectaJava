package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class RutasPagina {

    @Autowired
    private ProductService productService;

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
    public String mostrarDashBoard(Model model) {
        List<ProductDTO> productos = productService.listarProducts();
        model.addAttribute("productos", productos);
        return "DashBoard/DashBoardd"; // templates/DashBoard/DashBoardd.html
    }

}
