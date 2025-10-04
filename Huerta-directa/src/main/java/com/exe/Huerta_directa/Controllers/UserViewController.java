package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/editar_usuario/{id}")
    public String editarUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            UserDTO usuario = userService.obtenerUserPorId(id);
            model.addAttribute("usuario", usuario);
            return "Consulta_usuarios/editar_usuario";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado: " + e.getMessage());
            return "redirect:/consulta_usuarios";
        }
    }

    @PostMapping("/actualizar_usuario/{id}")
    @Transactional
    public String actualizarUsuario(@PathVariable Long id,
                                   @ModelAttribute UserDTO usuarioDTO,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Asegurarse de que el ID esté establecido
            usuarioDTO.setId(id);
            
            // Obtener el usuario actual
            UserDTO usuarioActual = userService.obtenerUserPorId(id);
            
            // Si la contraseña está vacía, mantener la actual
            if (usuarioDTO.getPassword() == null || usuarioDTO.getPassword().trim().isEmpty()) {
                usuarioDTO.setPassword(usuarioActual.getPassword());
            }
            
            // Actualizar el usuario
            UserDTO usuarioActualizado = userService.actualizarUser(id, usuarioDTO);
            
            redirectAttributes.addFlashAttribute("success",
                "Usuario '" + usuarioActualizado.getName() + "' actualizado exitosamente");
                
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error",
                "Error al actualizar el usuario: " + e.getMessage());
        }
        return "redirect:/consulta_usuarios";
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