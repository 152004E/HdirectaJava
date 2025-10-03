package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Service.ProductService;
import com.exe.Huerta_directa.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RutasPagina {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    /**
     * Método helper para obtener la página de inicio según el rol del usuario
     */
    private String getHomePageByUserRole(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null && user.getRole() != null) {
            // Si es admin (rol 1), redirigir al dashboard admin
            if (user.getRole().getIdRole() == 1) {
                return "redirect:/DashboardAdmin";
            } else if (user.getRole().getIdRole() == 2) {
                // Si es cliente (rol 2), redirigir al dashboard cliente
                return "redirect:/Dashboardd";
            }
        }
        // Por defecto, redirigir al index normal
        return "redirect:/index";
    }

    /**
     * Endpoint para redirección inteligente a inicio basada en sesión
     * Los botones que antes iban a /index ahora deben ir a /home
     */
    @GetMapping("/home")
    public String irAInicio(HttpSession session) {
        return getHomePageByUserRole(session);
    }

    @GetMapping({ "/", "/index" })
public String mostrarIndex(Model model, 
                         @ModelAttribute("success") String success) {
    // Log para debugging
    System.out.println("🔍 DEBUG: Mensaje de éxito recibido: " + success);
    
    // Obtener y agregar productos al modelo
    List<ProductDTO> productos = productService.listarProducts();
    System.out.println("Productos obtenidos: " + productos.size());
    model.addAttribute("productos", productos);
    
    // Si hay un mensaje de éxito, agregarlo al modelo
    if (success != null && !success.isEmpty()) {
        model.addAttribute("success", success);
    }

    return "index";
}

    @GetMapping("/agregar_producto")
    public String mostrarFormulario() {
        return "Agreagar_producto/Agregar_producto";
    }

    @GetMapping("/login")
public String mostrarLogin(Model model,
                          @RequestParam(value = "error", required = false) String error,
                          @RequestParam(value = "message", required = false) String message,
                          @RequestParam(value = "success", required = false) String success) {

    // Log para debugging - confirma que el controlador está siendo llamado
    System.out.println("🔍 DEBUG: Controlador /login ejecutándose correctamente");

    // Agregar un objeto UserDTO vacío al modelo para el formulario
    model.addAttribute("userDTO", new UserDTO());

    // Procesar mensaje de error de sesión si existe
    if ("session".equals(error) && message != null) {
        // Reemplazar '+' por espacios en el mensaje y agregarlo al modelo
        model.addAttribute("alertMessage", message.replace("+", " "));
    }

    // Procesar mensaje de éxito si existe
    if (success != null) {
        // Reemplazar '+' por espacios en el mensaje y agregarlo al modelo
        model.addAttribute("success", success.replace("+", " "));
    }

    // Retornar la vista login/login.html
    return "login/login";
}

    @GetMapping("/error404")
    public String mostrarerror404() {
        return "Errores/error404";
    }

    @GetMapping("/pasarelaPagos")
    public String mostrarPasarelaPagos() {
        return "Pasarela_Pagos/Pasarela";
    }

    @GetMapping("/error500")
    public String mostrarerror500() {
        return "Errores/error500";
    }

    @GetMapping("/actualizacionUsuario")
    public String actualizacionUsuario() {
        // Busca: src/main/resources/templates/Agregar_producto/Agregar_producto.html
        return "DashBoard/actualizacionUsuario";
    }

    @GetMapping("/landing")
    public String mostrarLanding() {
        return "pagina_principal/landing";
    }

    @GetMapping("/Quienes_somos")
    public String mostrarQuienes_somos() {
        return "Quienes_somos/quienes_somos";
    }

    @GetMapping("/Frutas")
    public String mostrarFrutas(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("frutas");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Frutas");
        return "ProductosCategorias/Frutas";
    }

    @GetMapping("/BebidasNaturales")
    public String mostrarBebidasNaturales(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("bebidas-naturales");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Bebidas Naturales");
        return "ProductosCategorias/BebidasNaturales";
    }

    @GetMapping("/CajasMixtas")
    public String mostrarCajasMixtas(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("cajas-combos");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Cajas Mixtas");
        return "ProductosCategorias/CajasMixtas";
    }

    @GetMapping("/CarnesYl")
    public String mostrarCarnesYl(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("carnes-proteinas");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Carnes y Proteínas");
        return "ProductosCategorias/CarnesYProteinas";
    }

    @GetMapping("/Cereales")
    public String mostrarCereales(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("cereales-granos");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Cereales y Granos");
        return "ProductosCategorias/CerealesYGranos";
    }

    @GetMapping("/Lacteos")
    public String mostrarLacteos(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("lacteos");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Lácteos");
        return "ProductosCategorias/Lacteos";
    }

    @GetMapping("/legumbresSecas")
    public String mostrarLegumbresSecas(Model model) {
        // CORRECCIÓN: Esta categoría estaba usando "bebidas-naturales"
        List<ProductDTO> productos = productService.listarProductsPorCategoria("legumbres-secas");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Legumbres Secas");
        return "ProductosCategorias/legumbresSecas";
    }

    @GetMapping("/MielYDerivados")
    public String mostrarMielYDerivados(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("miel-derivados");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Miel y Derivados");
        return "ProductosCategorias/MielYDerivados";
    }

    @GetMapping("/Organicos")
    public String mostrarOrganicos(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("productos-organicos");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Productos Orgánicos");
        return "ProductosCategorias/Organicos";
    }

    @GetMapping("/VerdurasYHortalizas")
    public String mostrarVerdurasYHortalizas(Model model) {
        List<ProductDTO> productos = productService.listarProductsPorCategoria("verduras-hortalizas");
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", "Verduras y Hortalizas");
        return "ProductosCategorias/VerdurasYHortalizas";
    }

    @GetMapping("/DashboardAdmin")
    public String dashboard(Model model, HttpSession session) {
        // ✅ VERIFICACIÓN DE SESIÓN EXPIRADA
        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            return "redirect:/login?error=session&message=Sesión+expirada.+Debe+iniciar+sesión+para+acceder+al+panel+de+administración";
        }

        // ✅ VERIFICACIÓN DE PERMISOS DE ADMINISTRADOR
        if (userSession.getRole() == null || userSession.getRole().getIdRole() != 1) {
            return "redirect:/login?error=access&message=Acceso+denegado.+Solo+administradores+pueden+acceder+a+este+panel";
        }

        List<UserDTO> usuarios = userService.listarUsers();
        model.addAttribute("usuarios", usuarios);

        // Obtener usuario de la sesión para mostrar información dinámica
        model.addAttribute("currentUser", userSession);
        // Determinar el nombre del rol para mostrar
        String roleName = "Usuario";
        if (userSession.getRole() != null) {
            roleName = userSession.getRole().getIdRole() == 1 ? "Administrador" : "Cliente";
        }
        model.addAttribute("userRole", roleName);

        return "Dashboard_Admin/DashboardAdmin";
    }

    @GetMapping("/producto/{id}")
    public String verProducto(@PathVariable("id") Long id, Model model) {
        ProductDTO producto = productService.obtenerProductPorId(id);
        model.addAttribute("producto", producto);
        return "Productos/product_detail"; // apunta a tu vista en templates/Productos/product_detail.html
    }

    @GetMapping("/agregar_admin")
    public String mostrarFormularioAdmin(Model model, HttpSession session) {
        // Verificar que solo admins puedan acceder
        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            return "redirect:/login?error=session&message=Debe+iniciar+sesión+para+acceder+a+esta+funcionalidad";
        }

        if (userSession.getRole() == null || userSession.getRole().getIdRole() != 1) {
            return "redirect:/DashboardAdmin?error=access&message=Acceso+denegado.+Solo+administradores+pueden+registrar+otros+administradores";
        }

        model.addAttribute("userDTO", new UserDTO());
        // Pasar información del admin actual para mostrar en el formulario
        model.addAttribute("currentAdmin", userSession.getName());
        return "Dashboard_Admin/Registro_nuevo_admin/form_registro_admin";
    }

    @PostMapping("/registrarAdmin")
    public String registrarAdmin(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        // Verificar que solo admins puedan registrar otros admins
        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            return "redirect:/login?error=session&message=Sesión+expirada";
        }

        if (userSession.getRole() == null || userSession.getRole().getIdRole() != 1) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado. Solo administradores pueden registrar otros administradores.");
            return "redirect:/DashboardAdmin";
        }

        if (result.hasErrors()) {
            // Si hay errores de validación, volver al formulario con errores
            return "Dashboard_Admin/Registro_nuevo_admin/form_registro_admin";
        }

        try {
            // Verificar que el email no exista ya usando un try-catch para manejar si el método no existe
            try {
                List<UserDTO> todosLosUsuarios = userService.listarUsers();
                boolean emailExiste = todosLosUsuarios.stream()
                        .anyMatch(u -> u.getEmail().equalsIgnoreCase(userDTO.getEmail()));

                if (emailExiste) {
                    redirectAttributes.addFlashAttribute("error",
                            "Ya existe un usuario con el correo: " + userDTO.getEmail());
                    return "redirect:/agregar_admin";
                }
            } catch (Exception e) {
                // Si falla la validación de email duplicado, continuar con el registro
                System.out.println("⚠️ No se pudo verificar email duplicado, continuando...");
            }

            // Asignar rol de administrador (ID 1) automáticamente
            userDTO.setIdRole(1L);

            // Establecer fecha de creación
            userDTO.setCreacionDate(java.time.LocalDate.now());

            UserDTO adminCreado = userService.crearUser(userDTO);

            // Log de la acción para auditoria
            System.out.println("🔐 ADMIN REGISTRADO: " + userSession.getName() +
                    " registró a " + adminCreado.getName() + " como administrador");

            redirectAttributes.addFlashAttribute("success",
                    "✅ Administrador '" + adminCreado.getName() + "' registrado exitosamente por " + userSession.getName());

            return "redirect:/DashboardAdmin";

        } catch (Exception e) {
            System.err.println("❌ Error al registrar administrador: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error",
                    "Error al registrar administrador: " + e.getMessage());
            return "redirect:/agregar_admin";
        }
    }

}