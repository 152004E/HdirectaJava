package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.PaymentRequestDTO;
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
