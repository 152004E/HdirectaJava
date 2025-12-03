
package com.exe.Huerta_directa.Controllers;
import com.exe.Huerta_directa.DTO.PaymentRequest;
import com.exe.Huerta_directa.Service.MercadoPagoServicePaymentRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.exe.Huerta_directa.DTO.CarritoItem;
import com.exe.Huerta_directa.Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/payments")

public class PaymentController {

    private final MercadoPagoServicePaymentRequest mercadoPagoService;
    private final ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PaymentController(MercadoPagoServicePaymentRequest mercadoPagoService, ProductService productService) {
        this.mercadoPagoService = mercadoPagoService;
        this.productService = productService;
    }

    @PostMapping("/process")
    public Map<String, Object> processPayment(
            @RequestBody PaymentRequest paymentRequest,
            HttpSession session) {

        try {
            System.out.println("📥 Iniciando procesamiento de pago...");
            System.out.println("💳 Método de pago: " + paymentRequest.getPaymentMethodId());
            System.out.println("💵 Monto: " + paymentRequest.getTransactionAmount());

            // Llamar a tu servicio (retorna String JSON)
            String responseJson = mercadoPagoService.processPayment(paymentRequest);

            System.out.println("📤 Respuesta de MercadoPago (JSON): " + responseJson);

            // ⭐ Parsear el JSON a Map usando Jackson
            Map<String, Object> paymentResult = objectMapper.readValue(responseJson, Map.class);

            // Extraer información importante
            String status = (String) paymentResult.get("status");
            Object paymentId = paymentResult.get("id");
            String paymentMethodId = (String) paymentResult.get("payment_method_id");

            System.out.println("📊 Estado del pago: " + status);
            System.out.println("🔢 Payment ID: " + paymentId);
            System.out.println("💳 Método usado: " + paymentMethodId);

            // ⭐ Si el pago fue aprobado, descontar stock
            if ("approved".equals(status)) {
                System.out.println("✅ Pago APROBADO - Descontando stock...");
                descontarStockDelCarrito(session);
                paymentResult.put("stock_descontado", true);
                paymentResult.put("mensaje", "Pago procesado exitosamente. Stock actualizado.");

            } else if ("pending".equals(status) || "in_process".equals(status)) {
                System.out.println("⏳ Pago PENDIENTE - No se descuenta stock aún");
                paymentResult.put("mensaje", "Pago pendiente de confirmación");

            } else if ("rejected".equals(status)) {
                System.out.println("❌ Pago RECHAZADO");
                paymentResult.put("mensaje", "El pago fue rechazado");
            }

            return paymentResult;

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            System.err.println("❌ Error parseando JSON de MercadoPago: " + e.getMessage());
            e.printStackTrace();
            return crearRespuestaError("Error al procesar la respuesta de MercadoPago");

        } catch (RuntimeException e) {
            System.err.println("❌ Error de MercadoPago: " + e.getMessage());
            e.printStackTrace();
            return crearRespuestaError(e.getMessage());

        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return crearRespuestaError("Error inesperado al procesar el pago");
        }
    }


    private void descontarStockDelCarrito(HttpSession session) {
        try {
            @SuppressWarnings("unchecked")
            List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");

            if (carrito == null || carrito.isEmpty()) {
                System.out.println("⚠️ No hay productos en el carrito para descontar");
                return;
            }

            System.out.println("📦 Descontando stock de " + carrito.size() + " productos:");

            for (CarritoItem item : carrito) {
                try {
                    productService.descontarStock(item.getProductId(), item.getCantidad());
                    System.out.println("  ✅ " + item.getNombre() + " - Cantidad: " + item.getCantidad());

                } catch (RuntimeException e) {
                    System.err.println("  ❌ Error con producto " + item.getNombre() + ": " + e.getMessage());
                    // Continuamos con los demás productos
                }
            }

            // Limpiar el carrito de la sesión
            session.removeAttribute("carrito");
            System.out.println("🗑️ Carrito limpiado de la sesión");

        } catch (Exception e) {
            System.err.println("❌ Error general al descontar stock: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("error", true);
        errorResponse.put("mensaje", mensaje);
        return errorResponse;
    }
}






/*import com.exe.Huerta_directa.DTO.PaymentRequestDTO;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Service.MercadoPagoService;
//import com.mercadopago.exceptions.MPApiException;
//import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

/*@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    /**
     * Mostrar página de checkout
     */

/*
    @GetMapping("/checkout")
    public String showCheckout(
            @RequestParam String productName,
            @RequestParam BigDecimal price,
            @RequestParam(defaultValue = "1") Integer quantity,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("productName", productName);
        model.addAttribute("price", price);
        model.addAttribute("quantity", quantity);
        model.addAttribute("total", price.multiply(BigDecimal.valueOf(quantity)));
        model.addAttribute("userEmail", user.getEmail());

        return "Pasarela_Pagos/checkout";
    }


    @PostMapping("/create-preference")
public String createPreference(
        @RequestParam String productName,
        @RequestParam BigDecimal price,
        @RequestParam Integer quantity,
        HttpSession session,
        RedirectAttributes redirectAttributes) {

    System.out.println("\nINICIO CREATE PREFERENCE");
    System.out.println(" Sesión ID: " + session.getId());

    User user = (User) session.getAttribute("user");

    if (user == null) {
        System.err.println("ERROR: Usuario no encontrado en sesión");
        System.err.println("   Redirigiendo a /login");
        return "redirect:/login";
    }

    System.out.println(" Usuario encontrado:");
    System.out.println("   - ID: " + user.getId());
    System.out.println("   - Email: " + user.getEmail());
    System.out.println("   - Nombre: " + user.getName());

    System.out.println("\n Datos del producto:");
    System.out.println("   - Producto: " + productName);
    System.out.println("   - Precio: $" + price);
    System.out.println("   - Cantidad: " + quantity);

    try {
        System.out.println("\n Creando PaymentRequestDTO...");
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setTitle(productName);
        paymentRequest.setPrice(price);
        paymentRequest.setQuantity(quantity);
        System.out.println(" DTO creado correctamente");

        System.out.println("\n Llamando a MercadoPagoService.createPreference()...");
        Preference preference = mercadoPagoService.createPreference(paymentRequest, user.getId());

        System.out.println("\nÉXITO");
        System.out.println("   - Preference ID: " + preference.getId());
        System.out.println("   - Init Point: " + preference.getSandboxInitPoint());
        System.out.println("   - Redirigiendo a Mercado Pago...");
        System.out.println("FIN CREATE PREFERENCE\n");

        return "redirect:" + preference.getSandboxInitPoint();

    } catch (Exception e) {
        System.err.println("\nERROR CAPTURADO");
        System.err.println("Clase de error: " + e.getClass().getName());
        System.err.println("Mensaje: " + e.getMessage());
        System.err.println("\n Stack trace completo:");
        e.printStackTrace();
        System.err.println(" FIN DEL ERROR\n");

        redirectAttributes.addFlashAttribute("error", "Error al procesar el pago: " + e.getMessage());
        return "redirect:/index";
    }
}

    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam(required = false) String payment_id,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("paymentId", payment_id);
        model.addAttribute("userName", user.getName());

        return "Pasarela_Pagos/success";
    }

    @GetMapping("/failure")
    public String paymentFailure(
            @RequestParam(required = false) String payment_id,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("paymentId", payment_id);
        model.addAttribute("userName", user.getName());

        return "Pasarela_Pagos/failure";
    }

    @GetMapping("/pending")
    public String paymentPending(
            @RequestParam(required = false) String payment_id,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("paymentId", payment_id);
        model.addAttribute("userName", user.getName());

        return "Pasarela_Pagos/pending";
    }

}

*/







/*import com.exe.Huerta_directa.DTO.PaymentRequestDTO;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Service.MercadoPagoService;
//import com.mercadopago.exceptions.MPApiException;
//import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

/*@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    /**
     * Mostrar página de checkout
     */

/*
    @GetMapping("/checkout")
    public String showCheckout(
            @RequestParam String productName,
            @RequestParam BigDecimal price,
            @RequestParam(defaultValue = "1") Integer quantity,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("productName", productName);
        model.addAttribute("price", price);
        model.addAttribute("quantity", quantity);
        model.addAttribute("total", price.multiply(BigDecimal.valueOf(quantity)));
        model.addAttribute("userEmail", user.getEmail());

        return "Pasarela_Pagos/checkout";
    }


    @PostMapping("/create-preference")
public String createPreference(
        @RequestParam String productName,
        @RequestParam BigDecimal price,
        @RequestParam Integer quantity,
        HttpSession session,
        RedirectAttributes redirectAttributes) {

    System.out.println("\nINICIO CREATE PREFERENCE");
    System.out.println(" Sesión ID: " + session.getId());

    User user = (User) session.getAttribute("user");

    if (user == null) {
        System.err.println("ERROR: Usuario no encontrado en sesión");
        System.err.println("   Redirigiendo a /login");
        return "redirect:/login";
    }

    System.out.println(" Usuario encontrado:");
    System.out.println("   - ID: " + user.getId());
    System.out.println("   - Email: " + user.getEmail());
    System.out.println("   - Nombre: " + user.getName());

    System.out.println("\n Datos del producto:");
    System.out.println("   - Producto: " + productName);
    System.out.println("   - Precio: $" + price);
    System.out.println("   - Cantidad: " + quantity);

    try {
        System.out.println("\n Creando PaymentRequestDTO...");
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setTitle(productName);
        paymentRequest.setPrice(price);
        paymentRequest.setQuantity(quantity);
        System.out.println(" DTO creado correctamente");

        System.out.println("\n Llamando a MercadoPagoService.createPreference()...");
        Preference preference = mercadoPagoService.createPreference(paymentRequest, user.getId());

        System.out.println("\nÉXITO");
        System.out.println("   - Preference ID: " + preference.getId());
        System.out.println("   - Init Point: " + preference.getSandboxInitPoint());
        System.out.println("   - Redirigiendo a Mercado Pago...");
        System.out.println("FIN CREATE PREFERENCE\n");

        return "redirect:" + preference.getSandboxInitPoint();

    } catch (Exception e) {
        System.err.println("\nERROR CAPTURADO");
        System.err.println("Clase de error: " + e.getClass().getName());
        System.err.println("Mensaje: " + e.getMessage());
        System.err.println("\n Stack trace completo:");
        e.printStackTrace();
        System.err.println(" FIN DEL ERROR\n");

        redirectAttributes.addFlashAttribute("error", "Error al procesar el pago: " + e.getMessage());
        return "redirect:/index";
    }
}

    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam(required = false) String payment_id,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("paymentId", payment_id);
        model.addAttribute("userName", user.getName());

        return "Pasarela_Pagos/success";
    }

    @GetMapping("/failure")
    public String paymentFailure(
            @RequestParam(required = false) String payment_id,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("paymentId", payment_id);
        model.addAttribute("userName", user.getName());

        return "Pasarela_Pagos/failure";
    }

    @GetMapping("/pending")
    public String paymentPending(
            @RequestParam(required = false) String payment_id,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("paymentId", payment_id);
        model.addAttribute("userName", user.getName());

        return "Pasarela_Pagos/pending";
    }

}

*/
