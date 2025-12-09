package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.CarritoItem;
import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CarritoController {

    private final ProductService productService;

    private static final String CARRITO_SESSION = "carrito";
    private static final BigDecimal IVA_PERCENT = new BigDecimal("0.19"); // 19%
    private static final BigDecimal DESCUENTO_PERCENT = new BigDecimal("0.10"); // 10%

    /**
     * 🔹 ENDPOINT 1: Guardar carrito en sesión (llamado desde tu carrito.js)
     */
    /**
     * 🔹 ENDPOINT 1: Guardar carrito en sesión (llamado desde tu carrito.js)
     */

    @PostMapping("/carrito/guardar-sesion")
    public ResponseEntity<Map<String, Object>> guardarCarritoEnSesion(
            @RequestBody List<Map<String, Object>> productosJS,
            HttpSession session) {
        try {
            System.out.println("/carrito/guardar-sesion");
            System.out.println("Productos recibidos: " + productosJS.size());

            List<CarritoItem> carrito = new ArrayList<>();

            for (Map<String, Object> prod : productosJS) {
                Long productId = Long.parseLong(prod.get("id").toString());
                Integer cantidad = Integer.parseInt(prod.get("cantidad").toString());

                // Validar que el producto existe y tiene stock
                ProductDTO producto = productService.obtenerProductPorId(productId);
                if (producto == null) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "status", "error",
                            "message", "Producto no encontrado: " + productId
                    ));
                }

                if (cantidad > producto.getStock()) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "status", "error",
                            "message", "Stock insuficiente, " + producto.getNameProduct()+ " disponible: " + producto.getStock()
                    ));
                }

                CarritoItem item = new CarritoItem();
                item.setProductId(productId);
                item.setNombre(prod.get("nombre").toString());
                item.setPrecio(new BigDecimal(prod.get("precio").toString()));
                item.setCantidad(cantidad);
                item.setDescripcion(producto.getDescriptionProduct());
                item.setImagen(producto.getImageProduct());

                carrito.add(item);
            }

            session.setAttribute(CARRITO_SESSION, carrito);
            System.out.println("✅ Carrito guardado en sesión: " + carrito.size() + " productos");

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Carrito guardado correctamente",
                    "productos", carrito.size()
            ));

        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Error interno: " + e.getMessage()
            ));
        }
    }


    /**
     * 🔹 ENDPOINT 2: Mostrar resumen del carrito
     *
     */
    @GetMapping("/carrito/resumen")
    public String verResumen(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute(CARRITO_SESSION);

        if (carrito == null || carrito.isEmpty()) {
            model.addAttribute("mensaje", "Tu carrito está vacío");
            return "redirect:/index";
        }

        // Calcular totales
        BigDecimal subtotal = calcularSubtotal(carrito);
        BigDecimal descuento = subtotal.multiply(DESCUENTO_PERCENT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotalConDescuento = subtotal.subtract(descuento);
        BigDecimal iva = subtotalConDescuento.multiply(IVA_PERCENT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalConDescuento.add(iva);

        System.out.println("💰 Subtotal: " + subtotal);
        System.out.println("💸 Descuento: " + descuento);
        System.out.println("📊 IVA: " + iva);
        System.out.println("💵 TOTAL: " + total);

        // Pasar datos a la vista
        model.addAttribute("productos", carrito);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuento", descuento);
        model.addAttribute("descuentoPercent", DESCUENTO_PERCENT.multiply(BigDecimal.valueOf(100)).intValue());
        model.addAttribute("iva", iva);
        model.addAttribute("ivaPercent", IVA_PERCENT.multiply(BigDecimal.valueOf(100)).intValue());
        model.addAttribute("total", total);

        return "Modulo_Pagos/Resumen_Pago";
    }

    /**
     * 🔹 ENDPOINT 3: Redirigir al Brick con el total calculado
     *
     */
    @GetMapping("/carrito/checkout")
    public String mostrarCheckout(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute(CARRITO_SESSION);

        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/index";
        }

        // Calcular total
        BigDecimal subtotal = calcularSubtotal(carrito);
        BigDecimal descuento = subtotal.multiply(DESCUENTO_PERCENT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotalConDescuento = subtotal.subtract(descuento);
        BigDecimal iva = subtotalConDescuento.multiply(IVA_PERCENT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalConDescuento.add(iva);

        // Guardar el total en la sesión para el Brick
        session.setAttribute("total_pago", total);

        // Crear descripción de productos
        StringBuilder descripcion = new StringBuilder("Productos: ");
        for (int i = 0; i < carrito.size(); i++) {
            CarritoItem item = carrito.get(i);
            descripcion.append(item.getNombre());
            if (i < carrito.size() - 1) {
                descripcion.append(", ");
            }
        }

        // Pasar datos al Brick
        model.addAttribute("total", total);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuento", descuento);
        model.addAttribute("iva", iva);
        model.addAttribute("descripcion", descripcion.toString());
        model.addAttribute("productos", carrito);

        return "MercadoPago/checkout";
    }

    /**
     * 🔹 ENDPOINT 4: API para obtener datos del carrito (usado por JavaScript)
     */
    @GetMapping("/api/carrito/datos")
    @ResponseBody
    public Map<String, Object> obtenerDatosCarrito(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute(CARRITO_SESSION);

        if (carrito == null || carrito.isEmpty()) {
            return Map.of("error", "Carrito vacío");
        }

        // Calcular totales
        BigDecimal subtotal = calcularSubtotal(carrito);
        BigDecimal descuento = subtotal.multiply(DESCUENTO_PERCENT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotalConDescuento = subtotal.subtract(descuento);
        BigDecimal iva = subtotalConDescuento.multiply(IVA_PERCENT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalConDescuento.add(iva);

        // Preparar respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("productos", carrito);
        response.put("subtotal", subtotal.doubleValue());
        response.put("descuento", descuento.doubleValue());
        response.put("iva", iva.doubleValue());
        response.put("total", total.doubleValue());
        response.put("descuentoPercent", DESCUENTO_PERCENT.multiply(BigDecimal.valueOf(100)).intValue());
        response.put("ivaPercent", IVA_PERCENT.multiply(BigDecimal.valueOf(100)).intValue());

        return response;
    }

    /**
     * 🔹 Metodo auxiliar para calcular subtotal
     */
    private BigDecimal calcularSubtotal(List<CarritoItem> carrito) {
        return carrito.stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}