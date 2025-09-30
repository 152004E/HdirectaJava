package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DashboardController {

    private final ProductService productService;

    public DashboardController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/Dashboardd")
    public String dashboard(Model model,
                          @RequestParam(required = false) String buscar,
                          @RequestParam(required = false) String categoria) {
        List<ProductDTO> productos;
        
        if (buscar != null && !buscar.isEmpty()) {
            productos = productService.buscarPorNombre(buscar);
        } else if (categoria != null && !categoria.isEmpty() && !categoria.equals("Por categoría")) {
            productos = productService.buscarPorCategoria(categoria);
        } else {
            productos = productService.listarProducts();
        }
        
        model.addAttribute("productos", productos);
        return "DashBoard/Dashboardd";  // <-- CAMBIADO: Carpeta/Archivo
    }

    @GetMapping("/editar_producto/{id}")
    public String editarProducto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ProductDTO producto = productService.obtenerProductPorId(id);
            model.addAttribute("producto", producto);
            return "DashBoard/editar_producto";  // <-- CAMBIADO
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/Dashboardd";
        }
    }

    @PostMapping("/actualizar_producto/{id}")
    public String actualizarProducto(@PathVariable Long id, 
                                    @ModelAttribute ProductDTO producto,
                                    RedirectAttributes redirectAttributes) {
        try {
            productService.actualizarProduct(id, producto);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto");
        }
        return "redirect:/Dashboardd";
    }

    @PostMapping("/eliminar_producto/{id}")
    public String eliminarProductoPost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.eliminarProductPorId(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto");
        }
        return "redirect:/Dashboardd";
    }
}