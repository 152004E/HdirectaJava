package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
                          @RequestParam(required = false) String categoria,
                          HttpSession session) {

        // Obtener usuario de la sesión para mostrar información dinámica
        User userSession = (User) session.getAttribute("user");
        if (userSession != null) {
            model.addAttribute("currentUser", userSession);
            // Determinar el nombre del rol para mostrar
            String roleName = "Usuario";
            if (userSession.getRole() != null) {
                roleName = userSession.getRole().getIdRole() == 1 ? "Administrador" : "Cliente";
            }
            model.addAttribute("userRole", roleName);
        }

        List<ProductDTO> productos;
        
        // Lógica de filtrado mejorada
        if (buscar != null && !buscar.trim().isEmpty()) {
            productos = productService.buscarPorNombre(buscar.trim());
            model.addAttribute("buscarActivo", buscar);
        } else if (categoria != null && !categoria.isEmpty() && !categoria.equals("Por categoría")) {
            productos = productService.buscarPorCategoria(categoria);
            model.addAttribute("categoriaActiva", categoria);
        } else {
            productos = productService.listarProducts();
        }
        
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        
        return "DashBoard/Dashboardd";
    }

    @GetMapping("/editar_producto/{id}")
    public String editarProducto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ProductDTO producto = productService.obtenerProductPorId(id);
            model.addAttribute("producto", producto);
            return "DashBoard/editar_producto";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado: " + e.getMessage());
            return "redirect:/Dashboardd";
        }
    }

    @PostMapping("/actualizar_producto/{id}")
    @Transactional
    public String actualizarProducto(@PathVariable Long id, 
                                    @ModelAttribute ProductDTO productoDTO,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Asegurarse de que el ID esté establecido
            productoDTO.setIdProduct(id);
            
            // Obtener el producto actual para mantener campos que no se editan
            ProductDTO productoActual = productService.obtenerProductPorId(id);
            
            // Mantener la imagen si no se cambió
            if (productoDTO.getImageProduct() == null || productoDTO.getImageProduct().isEmpty()) {
                productoDTO.setImageProduct(productoActual.getImageProduct());
            }
            
            // Mantener la fecha de publicación
            if (productoDTO.getPublicationDate() == null) {
                productoDTO.setPublicationDate(productoActual.getPublicationDate());
            }
            
            // Mantener el userId
            if (productoDTO.getUserId() == null) {
                productoDTO.setUserId(productoActual.getUserId());
            }
            
            // Actualizar el producto
            ProductDTO productoActualizado = productService.actualizarProduct(id, productoDTO);
            
            redirectAttributes.addFlashAttribute("success", 
                "Producto '" + productoActualizado.getNameProduct() + "' actualizado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al actualizar el producto: " + e.getMessage());
        }
        return "redirect:/Dashboardd";
    }

    @PostMapping("/eliminar_producto/{id}")
    @Transactional
    public String eliminarProductoPost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ProductDTO producto = productService.obtenerProductPorId(id);
            productService.eliminarProductPorId(id);
            redirectAttributes.addFlashAttribute("success", 
                "Producto '" + producto.getNameProduct() + "' eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/Dashboardd";
    }
}