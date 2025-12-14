package com.exe.Huerta_directa.Controllers;


import com.exe.Huerta_directa.Service.DeliveryService;
import com.exe.Huerta_directa.Strategy.DeliveryResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * Vista inicial del formulario
     */
    @GetMapping("/delivery/")
    public String showForm() {
        return "delivery-form"; // tu formulario
    }

    /**
     * Procesa la consulta de entrega
     */
    @PostMapping("/delivery/calculate")
    public String calculateDelivery(
            @RequestParam String type,
            @RequestParam String localidad,
            Model model
    ) {
        DeliveryResult result;

        try {
            result = deliveryService.processDelivery(type, localidad);
        } catch (IllegalArgumentException ex) {

            // Manejo de error limpio
            model.addAttribute("canDeliver", false);
            model.addAttribute("vehicleName", type);
            model.addAttribute("restrictions", ex.getMessage());

            return "Delivery_Form/delivery-result"; // MISMA vista
        }

        // =========================
        // Datos para el HTML
        // =========================
        model.addAttribute("canDeliver", result.isCanDeliver());
        model.addAttribute("vehicleName", result.getVehicleName());
        model.addAttribute("localidad", result.getLocalidad());
        model.addAttribute("estimatedTime", result.getEstimatedTime());
        model.addAttribute("percentage", result.getPercentage());
        model.addAttribute("formattedTime", result.getFormattedTime());
        model.addAttribute("restrictions", result.getRestrictions());

        return "Delivery_Form/delivery-result";
    }
}
