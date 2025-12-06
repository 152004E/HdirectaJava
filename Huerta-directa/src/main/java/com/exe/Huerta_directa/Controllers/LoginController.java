package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Entity.Role;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.UserService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.Period;
import java.util.Properties;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/api/login")
@CrossOrigin("*")
public class LoginController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    // logger
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    public LoginController(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_PORT = "587";
    private static final String SENDER_EMAIL = "hdirecta@gmail.com";
    // Nota: la contraseña de aplicación idealmente debe guardarse en
    // properties/secret manager
    private static final String SENDER_PASSWORD = "agst ebgg yakk lohu";

    // Metodo para crear la sesion de correo
    private Session crearSesionCorreo() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", EMAIL_HOST);
        props.put("mail.smtp.port", EMAIL_PORT);
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
    }

    @PostMapping("/register")
    public String seveUserView(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (result.hasErrors()) {
            return "login/login";
        }
        try {
            // Validar edad mínima (18 años)
            if (userDTO.getBirthDate() != null) {
                LocalDate today = LocalDate.now();
                Period age = Period.between(userDTO.getBirthDate(), today);
                if (age.getYears() < 18) {
                    redirectAttributes.addFlashAttribute("error", "Debes ser mayor de 18 años para registrarte");
                    redirectAttributes.addFlashAttribute("userDTO", userDTO);
                    return "redirect:/login";
                }
            }
            // NO hashear aqui: quitar la siguiente lnea si existe en tu controlador
             //userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            // Dejar que el servicio se encargue de hashear:
            UserDTO usuarioCreado = userService.crearUser(userDTO);
            User userEntity = userRepository.findByEmail(usuarioCreado.getEmail()).orElse(null);
            if (userEntity != null) {
                session.setAttribute("user", userEntity);
            } else {
                session.setAttribute("user", convertirDTOaEntity(usuarioCreado));
            }
            enviarCorreoConfirmacion(usuarioCreado.getName(), usuarioCreado.getEmail());
            if (usuarioCreado.getIdRole() != null && usuarioCreado.getIdRole() == 1L) {
                redirectAttributes.addFlashAttribute("success", "¡Bienvenido Administrador!");
                return "redirect:/DashboardAdmin";
            } else {
                redirectAttributes.addFlashAttribute("success", "¡Bienvenido!");
                return "redirect:/index";
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "El correo electronico ya esta¡ registrado");
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            return "redirect:/login";
        } catch (Exception e) {
            log.error("Error al crear la cuenta", e);
            redirectAttributes.addFlashAttribute("error", "Error al crear la cuenta.");
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            return "redirect:/login";
        }
    }

        // Metodo para enviar correo de confirmacion de que si se registro
        private void enviarCorreoConfirmacion(String nombre, String email) throws MessagingException {
            Session session = crearSesionCorreo();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Registro exitoso en Huerta Directa");
            // Crear el contenido HTML del correo
            String htmlContent = crearContenidoHTMLCorreo(nombre);
            // Configurar el mensaje como HTML
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
        }
        // CONTENIDO HTML DEL CORREO DE REGISTRO
        private String crearContenidoHTMLCorreo(String nombre) {
            return """
                    <!DOCTYPE html>
                    <html lang="es">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Bienvenido a Huerta Directa</title>
                    </head>
                    <body style="margin: 0; padding: 0; font-family: 'Arial', sans-serif; background-color: #f4f4f4;">
                        <table role="presentation" style="width: 100%%; border-collapse: collapse;">
                            <tr>
                                <td style="padding: 0;">
                                    <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
                                        <!-- Header con gradiente verde -->
                                        <div style="background: linear-gradient(135deg, #689f38 0%%, #8bc34a 100%%); padding: 40px 30px; text-align: center;">
                                            <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold; text-shadow: 0 2px 4px rgba(0,0,0,0.3);">
                                                🌱 Huerta Directa
                                            </h1>
                                            <p style="color: #ffffff; margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;">
                                                Conectando el campo con tu mesa
                                            </p>
                                        </div>
                                        <!-- Contenido principal -->
                                        <div style="padding: 40px 30px;">
                                            <div style="text-align: center; margin-bottom: 30px;">
                                                <div style="background-color: #e8f5e8; border-radius: 50px; width: 80px; height: 80px; margin: 0 auto 20px auto; display: flex; align-items: center; justify-content: center; font-size: 35px;">
                                                    ✅
                                                </div>
                                                <h2 style="color: #2e7d32; margin: 0; font-size: 24px; font-weight: bold;">
                                                    ¡Registro Exitoso!
                                                </h2>
                                            </div>
                                            <div style="text-align: center; margin-bottom: 30px;">
                                                <p style="color: #333333; font-size: 18px; line-height: 1.6; margin-bottom: 15px;">
                                                    ¡Hola <strong style="color: #689f38;">%s</strong>! 👋
                                                </p>
                                                <p style="color: #666666; font-size: 16px; line-height: 1.6; margin-bottom: 20px;">
                                                    Tu cuenta en <strong>Huerta Directa</strong> ha sido creada exitosamente.
                                                    Ahora formas parte de nuestra comunidad que conecta directamente a productores
                                                    campesinos con consumidores como tú.
                                                </p>
                                            </div>
                                            <!-- Beneficios -->
                                            <div style="background-color: #f8f9fa; border-radius: 8px; padding: 25px; margin-bottom: 30px;">
                                                <h3 style="color: #2e7d32; margin: 0 0 20px 0; font-size: 18px; text-align: center;">
                                                    ¿Qué puedes hacer ahora?
                                                </h3>
                                                <div style="text-align: left;">
                                                    <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                        🥕 <strong>Explorar productos frescos</strong> directamente de la huerta
                                                    </p>
                                                    <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                        🚚 <strong>Realizar pedidos</strong> con entrega a domicilio
                                                    </p>
                                                    <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                        👨‍🌾 <strong>Conocer a los productores</strong> detrás de tus alimentos
                                                    </p>
                                                    <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                        💚 <strong>Apoyar la agricultura local</strong> y sostenible
                                                    </p>
                                                </div>
                                            </div>
                                            <!-- Botón de acción -->
                                            <div style="text-align: center; margin-bottom: 30px;">
                                                <a href="#" style="display: inline-block; background: linear-gradient(135deg, #689f38 0%%, #8bc34a 100%%); color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 25px; font-weight: bold; font-size: 16px; box-shadow: 0 4px 15px rgba(104, 159, 56, 0.3); transition: all 0.3s ease;">
                                                    🌟 Comenzar a Explorar
                                                </a>
                                            </div>
                                            <!-- Mensaje de agradecimiento -->
                                            <div style="text-align: center; border-top: 2px solid #e8f5e8; padding-top: 25px;">
                                                <p style="color: #666666; font-size: 14px; line-height: 1.5; margin: 0;">
                                                    Gracias por unirte a nuestra misión de acercar el campo a tu mesa.<br>
                                                    <strong style="color: #689f38;">¡Juntos construimos un futuro más verde! 🌍</strong>
                                                </p>
                                            </div>
                                        </div>
                                        <!-- Footer -->
                                        <div style="background-color: #2e7d32; padding: 25px 30px; text-align: center;">
                                            <p style="color: #ffffff; margin: 0 0 10px 0; font-size: 16px; font-weight: bold;">
                                                El equipo de Huerta Directa 🌱
                                            </p>
                                            <p style="color: #c8e6c9; margin: 0; font-size: 12px;">
                                                Este correo fue enviado automáticamente. Por favor, no respondas a este mensaje.
                                            </p>
                                            <div style="margin-top: 15px;">
                                                <span style="color: #c8e6c9; font-size: 12px;">
                                                    © 2024 Huerta Directa - Todos los derechos reservados
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </body>
                    </html>
                    """
                    .formatted(nombre);
        }

    @PostMapping("/loginUser")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        // Buscar el usuario por correo
        User user = userRepository.findByEmail(email).orElse(null);

        // ⚠Validar si el usuario existe y si la contraseña coincide
        if (user == null) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            model.addAttribute("userDTO", new UserDTO());
            return "login/login";
        }

        // Verificar con BCrypt si la contraseña plana coincide con el hash
        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            model.addAttribute("userDTO", new UserDTO());
            return "login/login";
        }

        //  GUARDAR EL USUARIO PRIMERO
        session.setAttribute("user", user);

        if (user.getPhone() == null || user.getPhone().isBlank()) {
            return "redirect:/api/login/redirigirPorRol";
        }
        session.setAttribute("pendingUser", user);
        return "login/verify-sms";  // ✅ Vista HTML
    }


    // =========================
    // CONFIRMAR SMS
    // =========================
    @PostMapping("/complete-login")
    public String completeLogin(HttpSession session) {
        User user = (User) session.getAttribute("pendingUser");
        if (user == null) return "redirect:/api/login/loginUser";

        session.setAttribute("user", user);
        session.removeAttribute("pendingUser");
        return "redirect:/api/login/redirigirPorRol";
    }

    @GetMapping("/redirigirPorRol")
    public String redirigirPorRol(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/api/login/loginUser";

        if (user.getRole().getIdRole() == 1) {
            return "redirect:/DashboardAdmin";
        } else {
            return "redirect:/index";
        }
    }

    /**
     * Metodo para obtener información del usuario logueado (útil para mostrar en el
     * frontend)
     */
    @GetMapping("/current")
    @ResponseBody
    public ResponseEntity<UserDTO> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setIdRole(user.getRole() != null ? user.getRole().getIdRole() : null);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Metodo para cerrar sesión
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destruir completamente la sesión
        return "redirect:/login";
    }


    // Registro de nuevo administrador desde el dashboard admin
    @PostMapping("/FormAdmin")
    public String registrarAdmin(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            // Cambiado para usar la plantilla existente en el proyecto
            return "Dashboard_Admin/Registro_nuevo_admin/form_registro_admin";
        }
        userService.crearAdmin(userDTO); // crear el admin
        redirect.addFlashAttribute("success", "Administrador creado con Ã©xito");
        return "redirect:/DashboardAdmin"; // redireccion al dashboard
    }

    @PostMapping("/forgot-password")
    public String solicitarRecuperacionContrasena(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                // Por seguridad, no revelamos si el email existe o no, pero mostramos el mismo
                // mensaje
                redirectAttributes.addFlashAttribute("success",
                        "Si el correo existe, recibiras tu nueva contraseña en unos minutos");
                return "redirect:/forgot-password";
            }
            // Generar nueva contraseña aleatoria
            String nuevaContrasena = generarContrasenaAleatoria();
            // Se hashea la nueva contraseña antes de guardarla
            user.setPassword(passwordEncoder.encode(nuevaContrasena));
            userRepository.save(user);
            // Enviar correo con la nueva contraseÃ±a
            enviarCorreoNuevaContrasena(user.getName(), email, nuevaContrasena);
            redirectAttributes.addFlashAttribute("success",
                    "Si el correo existe, recibiras tu nueva contraseña en unos minutos");
            return "redirect:/forgot-password";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al procesar la solicitud. Por favor, intenta nuevamente");
            return "redirect:/forgot-password";
        }
    }
    /**
     * Metodo para generar una contraseÃ±a aleatoria segura
     */
    private String generarContrasenaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        java.util.Random random = new Random();
        StringBuilder contrasena = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(caracteres.length());
            contrasena.append(caracteres.charAt(index));
        }
        return contrasena.toString();
    }
    /**
     * Metodo para enviar correo con la nueva contraseÃ±a
     */
    private void enviarCorreoNuevaContrasena(String nombre, String email, String nuevaContrasena)
            throws MessagingException {
        Session session = crearSesionCorreo();
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Tu nueva contraseña - Huerta Directa");
        String htmlContent = crearContenidoHTMLNuevaContrasena(nombre, nuevaContrasena);
        message.setContent(htmlContent, "text/html; charset=utf-8");
        Transport.send(message);
    }
    /**
     * Metodo para crear el contenido HTML del correo con la nueva contraseÃ±a
     */
    private String crearContenidoHTMLNuevaContrasena(String nombre, String nuevaContrasena) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Nueva Contraseña - Huerta Directa</title>
                </head>
                <body style="margin: 0; padding: 0; font-family: 'Arial', sans-serif; background-color: #f4f4f4;">
                    <table role="presentation" style="width: 100%%; border-collapse: collapse;">
                        <tr>
                            <td style="padding: 0;">
                                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
                                    <!-- Header -->
                                    <div style="background: linear-gradient(135deg, #689f38 0%%, #8bc34a 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">
                                            🌱 Huerta Directa
                                        </h1>
                                        <p style="color: #ffffff; margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;">
                                            Nueva Contraseña Generada
                                        </p>
                                    </div>
                                    <!-- Contenido principal -->
                                    <div style="padding: 40px 30px;">
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <div style="background-color: #e8f5e8; border-radius: 50px; width: 80px; height: 80px; margin: 0 auto 20px auto; display: flex; align-items: center; justify-content: center; font-size: 35px;">
                                                🔑
                                            </div>
                                            <h2 style="color: #2e7d32; margin: 0; font-size: 24px; font-weight: bold;">
                                                ¡Nueva Contraseña Lista!
                                            </h2>
                                        </div>
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <p style="color: #333333; font-size: 18px; line-height: 1.6; margin-bottom: 15px;">
                                                ¡Hola <strong style="color: #689f38;">%s</strong>! 👋
                                            </p>
                                            <p style="color: #666666; font-size: 16px; line-height: 1.6; margin-bottom: 20px;">
                                                Hemos generado una nueva contraseña para tu cuenta en <strong>Huerta Directa</strong>.
                                                Ya puedes iniciar sesión con esta nueva contraseña.
                                            </p>
                                        </div>
                                        <!-- Nueva contraseña -->
                                        <div style="background-color: #f8f9fa; border: 2px dashed #8dc84b; border-radius: 15px; padding: 25px; margin-bottom: 30px; text-align: center;">
                                            <h3 style="color: #2e7d32; margin: 0 0 15px 0; font-size: 18px;">
                                                🔐 Tu Nueva Contraseña
                                            </h3>
                                            <div style="background-color: #ffffff; border: 2px solid #8dc84b; border-radius: 10px; padding: 15px; margin: 10px 0;">
                                                <span style="font-family: 'Courier New', monospace; font-size: 24px; font-weight: bold; color: #2e7d32; letter-spacing: 2px;">
                                                    %s
                                                </span>
                                            </div>
                                            <p style="color: #666666; font-size: 12px; margin: 10px 0 0 0;">
                                                Copia esta contraseña exactamente como aparece
                                            </p>
                                        </div>
                                        <!-- Instrucciones -->
                                        <div style="background-color: #fff3e0; border-radius: 8px; padding: 20px; margin-bottom: 30px;">
                                            <h3 style="color: #f57c00; margin: 0 0 15px 0; font-size: 16px; text-align: center;">
                                                📋 Próximos Pasos
                                            </h3>
                                            <div style="text-align: left;">
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    1️⃣ Ve a la página de inicio de sesión
                                                </p>
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    2️⃣ Usa tu email y esta nueva contraseña
                                                </p>
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    3️⃣ ¡Recomendamos cambiarla por una personalizada!
                                                </p>
                                            </div>
                                        </div>
                                        <!-- Botón de acción -->
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <a href="http://localhost:8085/login" style="display: inline-block; background: linear-gradient(135deg, #689f38 0%%, #8bc34a 100%%); color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 25px; font-weight: bold; font-size: 16px; box-shadow: 0 4px 15px rgba(104, 159, 56, 0.3);">
                                                🚀 Iniciar Sesión Ahora
                                            </a>
                                        </div>
                                        <!-- Mensaje de seguridad -->
                                        <div style="text-align: center; border-top: 2px solid #e8f5e8; padding-top: 25px;">
                                            <p style="color: #666666; font-size: 14px; line-height: 1.5; margin: 0;">
                                                Si no solicitaste este cambio, contacta inmediatamente con soporte.<br>
                                                <strong style="color: #689f38;">Tu cuenta está segura con nosotros 🛡️</strong>
                                            </p>
                                        </div>
                                    </div>
                                    <!-- Footer -->
                                    <div style="background-color: #2e7d32; padding: 25px 30px; text-align: center;">
                                        <p style="color: #ffffff; margin: 0 0 10px 0; font-size: 16px; font-weight: bold;">
                                            El equipo de Huerta Directa 🌱
                                        </p>
                                        <p style="color: #c8e6c9; margin: 0; font-size: 12px;">
                                            Este correo fue enviado automáticamente. Por favor, no respondas a este mensaje.
                                        </p>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """
                .formatted(nombre, nuevaContrasena);
    }

    //Convertir de DTO a Entity
    private User convertirDTOaEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setCreacionDate(userDTO.getCreacionDate());
        // Crear un Role básico si es necesario
        if (userDTO.getIdRole() != null) {
            Role role = new Role();
            role.setIdRole(userDTO.getIdRole());
            user.setRole(role);
        }
        return user;
    }
}
