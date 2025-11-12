package com.exe.Huerta_directa.config;

import com.exe.Huerta_directa.Entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalAttributes {

    @ModelAttribute
    public void addUserToModel(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        String userRole = null;
        if (currentUser != null && currentUser.getRole() != null) {
            userRole = currentUser.getRole().getName();
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userRole", userRole);
    }
}