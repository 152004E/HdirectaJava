package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Service.ProductService;
import com.exe.Huerta_directa.Service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController  // ← CAMBIO: RestController en lugar de Controller
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService, ProductService productService) {
        this.userService = userService;
    }

    // =====================================================
    // ENDPOINTS PARA API REST (JSON)
    // =====================================================

    // Endpoint para exportar usuarios a Excel
    @GetMapping("/export/excel")
    public void exportUsersToExcel(HttpServletResponse response) throws IOException {
        try {
            // Configurar la respuesta HTTP para descarga de archivo Excel
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            
            // Generar nombre de archivo con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "users_" + timestamp + ".xlsx";
            
            // Configurar header para descarga
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setHeader("Cache-Control", "no-cache");
            
            // CAMBIO: Llamar al método correcto (con el typo original)
            userService.exporUserstToExcel(response.getOutputStream());
            
            // Limpiar el buffer
            response.getOutputStream().flush();
            
        } catch (Exception e) {
            // En caso de error, devolver un error HTTP 500
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error al generar el archivo Excel: " + e.getMessage());
        }
    }

    // Endpoint de prueba
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Controller funcionando correctamente!");
    }

    // Endpoint para contar usuarios
    @GetMapping("/count")
    public ResponseEntity<String> getUserCount() {
        try {
            List<UserDTO> users = userService.listarUsers();
            return ResponseEntity.ok("Total de usuarios encontrados: " + users.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al contar usuarios: " + e.getMessage());
        }
    }

    // Método para listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserDTO>> listarUsers() {
        try {
            List<UserDTO> users = userService.listarUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método para obtener un usuario por su id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> obtenerUserPorId(@PathVariable Long userId) {
        try {
            UserDTO user = userService.obtenerUserPorId(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Método para crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UserDTO> crearUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO newUser = userService.crearUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Método para actualizar un usuario
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> actualizarUser(@PathVariable("userId") Long userId, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.actualizarUser(userId, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Método para eliminar un usuario por su id
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> eliminarUserPorId(@PathVariable("userId") Long userId) {
        try {
            userService.eliminarUserPorId(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

// =====================================================
// CONTROLLER SEPARADO PARA VISTAS WEB (si lo necesitas)
// =====================================================
@Controller  // ← Controller normal para vistas
@RequestMapping("/web/users")  // ← Ruta diferente para evitar conflictos
class UserWebController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String saveUserView(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "users/login";
        }
        userService.crearUser(userDTO);
        redirect.addFlashAttribute("success", "Usuario creado");
        return "redirect:/index";
    }

    @PostMapping("/login")
    public String loginUser(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "users/login";
        }
        // Lógica de autenticación aquí
        redirect.addFlashAttribute("success", "Inicio de sesión exitoso");
        return "redirect:/index";
    }
}