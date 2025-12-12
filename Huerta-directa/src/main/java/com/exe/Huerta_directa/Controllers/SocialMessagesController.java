package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.Entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SocialMessagesController {

    @GetMapping("/MensajesAreaSocial")
    public String MensajesAreaSocial(Model model, HttpSession session) {

        // Obtener el User que está guardado en la sesión
        User user = (User) session.getAttribute("user");

        // Si no hay usuario, redirigir a login
        if (user == null) {
            return "redirect:/login";
        }

        // Enviar el usuario actual a Thymeleaf
        model.addAttribute("currentUser", user);

        return "DashBoard/MensajesAreaSocial";
    }
}
