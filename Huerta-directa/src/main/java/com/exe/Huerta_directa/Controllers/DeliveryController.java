package com.exe.Huerta_directa.Controllers;



import com.exe.Huerta_directa.Service.DeliveryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/")
    public String showForm() {
        return "Delivery-Form/delivery_from"; // Thymeleaf template
    }

    @GetMapping("/calculate")
    @ResponseBody
    public String calculate(@RequestParam String type, @RequestParam String region) {
        return deliveryService.processDelivery(type, region);
    }
}