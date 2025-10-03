package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserViewController {

    @Autowired
    private UserService userService;

    @GetMapping("/consulta_usuarios")
    public String consultarUsuarios(
            Model model,
            @RequestParam(required = false) String dato,
            @RequestParam(required = false) String valor) {
        
        List<UserDTO> usuarios;
        
        if (dato != null && valor != null && !valor.isEmpty()) {
            // Realizar búsqueda específica
            usuarios = buscarUsuariosPorFiltro(dato, valor);
        } else {
            // Mostrar todos los usuarios
            usuarios = userService.listarUsers();
        }
        
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("datoSeleccionado", dato);
        model.addAttribute("valorBuscado", valor);
        
        return "Consulta_usuarios/Consulta_usuario";
    }
    
    private List<UserDTO> buscarUsuariosPorFiltro(String dato, String valor) {
        List<UserDTO> todosUsuarios = userService.listarUsers();
        
        return todosUsuarios.stream()
                .filter(usuario -> {
                    switch (dato) {
                        case "id":
                            try {
                                return usuario.getId().equals(Long.parseLong(valor));
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        case "name_user":
                            return usuario.getName() != null && 
                                   usuario.getName().toLowerCase().contains(valor.toLowerCase());
                        case "email":
                            return usuario.getEmail() != null && 
                                   usuario.getEmail().toLowerCase().contains(valor.toLowerCase());
                        case "role":
                            return filtrarPorRol(usuario, valor);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }
    
    private boolean filtrarPorRol(UserDTO usuario, String valor) {
        if (usuario.getIdRole() == null) {
            return false;
        }
        
        String valorLower = valor.toLowerCase().trim();
        Long roleId = usuario.getIdRole();
        
        // Buscar por ID del rol (1 o 2)
        try {
            Long valorRoleId = Long.parseLong(valorLower);
            if (roleId.equals(valorRoleId)) {
                return true;
            }
        } catch (NumberFormatException e) {
            // No es un número, continuar con búsqueda por nombre
        }
        
        // Buscar por nombre del rol
        if (roleId == 1 && (valorLower.contains("admin") || valorLower.contains("administrador"))) {
            return true;
        }
        
        if (roleId == 2 && (valorLower.contains("client") || valorLower.contains("usuario"))) {
            return true;
        }
        
        return false;
    }
}