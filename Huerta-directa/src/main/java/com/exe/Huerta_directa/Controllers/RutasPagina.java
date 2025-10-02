package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.DTO.UserDTO;
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

    @GetMapping({ "/", "/index" })
    public String mostrarIndex(Model model) {
        // agragar productos al main
        List<ProductDTO> productos = productService.listarProducts();
        System.out.println("Productos obtenidos: " + productos);
        model.addAttribute("productos", productos);

        return "index"; // busca templates/index.html
    }

    @GetMapping("/agregar_producto")
    public String mostrarFormulario() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "Agreagar_producto/Agregar_producto";
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "login/login";
    }

    @GetMapping("/error404")
    public String mostrarerror404() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "Errores/error404";
    }

    @GetMapping("/error500")
    public String mostrarerror500() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "Errores/error500";
    }
    @GetMapping("/actualizacionUsuario")
    public String actualizacionUsuario() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "DashBoard/actualizacionUsuario";
    }

    @GetMapping("/landing")
    public String mostrarLanding() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "pagina_principal/landing";
    }

    @GetMapping("/Quienes_somos")
    public String mostrarQuienes_somos() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "Quienes_somos/quienes_somos";
    }

    @GetMapping("/Frutas")
    public String mostrarFrutas(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("frutas");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/Frutas";
    }

    @GetMapping("/BebidasNaturales")
    public String mostrarBebidasNaturales(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("bebidas-naturales");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/BebidasNaturales";
    }

    @GetMapping("/CajasMixtas")
    public String mostrarCajasMixtas(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("cajas-combos");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/CajasMixtas";
    }

    @GetMapping("/CarnesYl")
    public String mostrarCarnesYl(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("carnes-proteinas");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/CarnesYProteinas";
    }

    @GetMapping("/Cereales")
    public String mostrarCereales(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("cereales-granos");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/CerealesYGranos";
    }

    @GetMapping("/Lacteos")
    public String mostrarLacteos(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("lacteos");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/Lacteos";
    }

    @GetMapping("/legumbresSecas")
    public String mostrarLegumbresSecas(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("bebidas-naturales");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/legumbresSecas";
    }

    @GetMapping("/MielYDerivados")
    public String mostrarMielYDerivados(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("miel-derivados");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/MielYDerivados";
    }

    @GetMapping("/Organicos")
    public String mostrarOrganicos(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("productos-organicos");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/Organicos";
    }

    @GetMapping("/VerdurasYHortalizas")
    public String mostrarVerdurasYHortalizas(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("verduras-hortalizas");
        model.addAttribute("productos", productos);
        return "ProductosCategorias/VerdurasYHortalizas";
    }

    @GetMapping("DashboardAdmin")
    public String mostrarDashboardAdmin(Model model) {
        List<ProductDTO> productos = productService.listarProducts();
        model.addAttribute("productos", productos);
        return "Dashboard_Admin/DashboardAdmin"; // templates/DashBoard/DashboardAdmin.html
    }
}
