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
@Controller
@RequestMapping("/payment") 
public class PaymentController {
    @Autowired
    private MercadoPagoService mercadoPagoService;
    /**
     * Mostrar pÃ¡gina de checkout
     */
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
    User user = (User) session.getAttribute("user");
    if (user == null) {
        return "redirect:/login";
    }
    try {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setTitle(productName);
        paymentRequest.setPrice(price);
        paymentRequest.setQuantity(quantity);
        Preference preference = mercadoPagoService.createPreference(paymentRequest, user.getId());
        return "redirect:" + preference.getSandboxInitPoint();
    } catch (Exception e) {
        e.printStackTrace();
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
