package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.UserDTO;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.Period;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/api/login")

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
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s#.,\\-_/()]+$");
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
        props.put("mail.smtp.ssl.trust", EMAIL_HOST);
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
    }

    /**
     * Endpoint para registro de usuarios
     * Acepta JSON desde React y retorna respuesta JSON
     */
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO, HttpSession session) {
        log.info("Intento de registro para el email: {}", userDTO.getEmail());
        
        try {
            // Validar datos básicos
            if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("El correo electrónico es requerido"));
            }

            if (userDTO.getName() == null || userDTO.getName().isBlank()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("El nombre es requerido"));
            }

            if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("La contraseña es requerida"));
            }

            // Validar edad
            if (userDTO.getBirthDate() != null) {
                LocalDate today = LocalDate.now();
                Period age = Period.between(userDTO.getBirthDate(), today);

                if (age.getYears() < 18) {
                    log.warn("Intento de registro de menor de edad: {}", userDTO.getEmail());
                    return ResponseEntity
                            .badRequest()
                            .body(new ErrorResponse("Debes ser mayor de 18 años para registrarte"));
                }
            }

            // Crear usuario
            UserDTO usuarioCreado = userService.crearUser(userDTO);
            log.info("Usuario creado exitosamente: {}", usuarioCreado.getEmail());

            // Buscar el usuario creado
            User userEntity = userRepository
                    .findByEmail(usuarioCreado.getEmail())
                    .orElse(null);

            if (userEntity != null) {
                session.setAttribute("user", userEntity);
                session.setAttribute("userId", userEntity.getId());
                session.setAttribute("userRole", userEntity.getRole() != null ? userEntity.getRole().getIdRole() : null);
            }

            // Enviar correo de confirmación (asíncrono para no bloquear)
            try {
                enviarCorreoConfirmacion(usuarioCreado.getName(), usuarioCreado.getEmail());
                log.info("Correo de confirmación enviado a: {}", usuarioCreado.getEmail());
            } catch (Exception e) {
                log.warn("No se pudo enviar el correo de confirmación a: {}", usuarioCreado.getEmail(), e);
            }

            // Preparar respuesta
            UserResponse response = new UserResponse();
            response.setId(usuarioCreado.getId());
            response.setName(usuarioCreado.getName());
            response.setEmail(usuarioCreado.getEmail());
            response.setIdRole(usuarioCreado.getIdRole());
            response.setMessage("Registro exitoso");

            return ResponseEntity.ok(response);

        } catch (DataIntegrityViolationException e) {
            log.warn("Intento de registro con correo duplicado: {}", userDTO.getEmail());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("El correo electrónico ya está registrado"));

        } catch (Exception e) {
            log.error("Error al crear la cuenta para: {}", userDTO.getEmail(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al crear la cuenta: " + e.getMessage()));
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

    /**
     * Endpoint para login de usuarios
     * Acepta JSON desde React y retorna respuesta JSON
     */
    @PostMapping("/loginUser")
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpSession session) {
        log.info("Intento de login para el email: {}", loginRequest.getEmail());
        
        try {
            // Validar que los datos no estén vacíos
            if (loginRequest.getEmail() == null || loginRequest.getEmail().isBlank()) {
                log.warn("Email vacío en el intento de login");
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("El correo electrónico es requerido"));
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank()) {
                log.warn("Contraseña vacía en el intento de login");
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("La contraseña es requerida"));
            }

            // Buscar usuario por email
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);

            if (user == null) {
                log.warn("Usuario no encontrado con email: {}", loginRequest.getEmail());
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Correo o contraseña incorrectos"));
            }

            // Validar contraseña
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                log.warn("Contraseña incorrecta para el usuario: {}", loginRequest.getEmail());
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Correo o contraseña incorrectos"));
            }

            log.info("Login exitoso para el usuario: {} con rol: {}", 
                    user.getEmail(), 
                    user.getRole() != null ? user.getRole().getIdRole() : "sin rol");

            // Guardar en sesión
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole() != null ? user.getRole().getIdRole() : null);

            // Si necesita verificación SMS
            if (user.getPhone() != null && !user.getPhone().isBlank()) {
                session.setAttribute("pendingUser", user);
                LoginResponse response = new LoginResponse();
                response.setStatus("verify-sms");
                response.setMessage("Se requiere verificación SMS");
                return ResponseEntity.ok(response);
            }

            // Preparar respuesta exitosa
            LoginResponse response = new LoginResponse();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setIdRole(user.getRole() != null ? user.getRole().getIdRole() : null);
            response.setStatus("success");
            response.setMessage("Login exitoso");

            // Determinar redirect según rol (1 = Admin, otros = Usuario normal)
            if (user.getRole() != null && user.getRole().getIdRole() == 1) {
                log.info("Redirigiendo a dashboard de administrador");
                response.setRedirect("/admin-dashboard");
            } else {
                log.info("Redirigiendo a página principal");
                response.setRedirect("/HomePage");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error inesperado en login para usuario: {}", loginRequest.getEmail(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al iniciar sesión: " + e.getMessage()));
        }
    }

    // =========================
    // GESTIÓN DE SESIÓN
    // =========================
    
    /**
     * Verificar sesión actual del usuario
     */
    @GetMapping("/session")
    @ResponseBody
    public ResponseEntity<?> checkSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            log.debug("No hay sesión activa");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("No hay sesión activa"));
        }

        log.info("Sesión activa para el usuario: {} con rol: {}", 
                user.getEmail(), 
                user.getRole() != null ? user.getRole().getIdRole() : "sin rol");

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setIdRole(user.getRole() != null ? user.getRole().getIdRole() : null);
        response.setStatus("active");
        response.setMessage("Sesión activa");

        return ResponseEntity.ok(response);
    }

    /**
     * Cerrar sesión
     */
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user != null) {
            log.info("Cerrando sesión para el usuario: {}", user.getEmail());
        }
        
        session.invalidate();
        
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada correctamente"));
    }

    /**
     * Confirmar login después de verificación SMS
     */
    @PostMapping("/complete-login")
    @ResponseBody
    public ResponseEntity<?> completeLogin(HttpSession session) {
        User user = (User) session.getAttribute("pendingUser");
        
        if (user == null) {
            log.warn("Intento de completar login sin usuario pendiente");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("No hay usuario pendiente de verificación"));
        }

        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole() != null ? user.getRole().getIdRole() : null);
        session.removeAttribute("pendingUser");

        log.info("Login completado para usuario: {}", user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setIdRole(user.getRole() != null ? user.getRole().getIdRole() : null);
        response.setStatus("success");
        response.setMessage("Login completado");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/redirigirPorRol")
    public ResponseEntity<?> redirigirPorRol(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");

        String redirectUrl = (user.getRole().getIdRole() == 1) ? "/DashboardAdmin" : "/index";
        return ResponseEntity.ok(java.util.Map.of("redirect", redirectUrl));
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
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setGender(user.getGender());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setIdRole(user.getRole() != null ? user.getRole().getIdRole() : null);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Obtener perfil del usuario autenticado según su sesión actual
     */
    @GetMapping("/profile")
    @ResponseBody
    public ResponseEntity<?> getProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("No hay sesión activa"));
        }

        ProfileResponse response = new ProfileResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setIdRole(user.getRole() != null ? user.getRole().getIdRole() : null);

        return ResponseEntity.ok(response);
    }

    /**
     * Actualizar datos del perfil del usuario autenticado
     */
    @PutMapping("/profile")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("No hay sesión activa"));
        }

        if (request.getName() == null || request.getName().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("El nombre es obligatorio"));
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("El correo electrónico es obligatorio"));
        }

        if (request.getPhone() == null || request.getPhone().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("El número de teléfono es obligatorio"));
        }

        String normalizedPhone = request.getPhone().trim();
        if (!PHONE_PATTERN.matcher(normalizedPhone).matches()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("El teléfono debe tener exactamente 10 dígitos numéricos"));
        }

        if (request.getAddress() == null || request.getAddress().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("La dirección es obligatoria"));
        }

        String normalizedAddress = request.getAddress().trim();
        if (!ADDRESS_PATTERN.matcher(normalizedAddress).matches()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(
                    "La dirección contiene caracteres no permitidos. Usa letras, números y símbolos como # . , - /"));
        }

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        User existingByEmail = userRepository.findByEmail(normalizedEmail).orElse(null);
        if (existingByEmail != null && !existingByEmail.getId().equals(sessionUser.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Ese correo electrónico ya está en uso"));
        }

        sessionUser.setName(request.getName().trim());
        sessionUser.setEmail(normalizedEmail);
        sessionUser.setPhone(normalizedPhone);
        sessionUser.setAddress(normalizedAddress);

        User savedUser = userRepository.save(sessionUser);
        session.setAttribute("user", savedUser);

        ProfileResponse response = new ProfileResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setPhone(savedUser.getPhone());
        response.setAddress(savedUser.getAddress());
        response.setIdRole(savedUser.getRole() != null ? savedUser.getRole().getIdRole() : null);

        return ResponseEntity.ok(response);
    }

    /**
     * Cambiar contraseña del usuario autenticado
     */
    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("No hay sesión activa"));
        }

        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("La contraseña actual es obligatoria"));
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("La nueva contraseña es obligatoria"));
        }

        if (request.getNewPassword().trim().length() < 6) {
            return ResponseEntity.badRequest().body(new ErrorResponse("La nueva contraseña debe tener mínimo 6 caracteres"));
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), sessionUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("La contraseña actual no es correcta"));
        }

        sessionUser.setPassword(passwordEncoder.encode(request.getNewPassword().trim()));
        userRepository.save(sessionUser);

        return ResponseEntity.ok(new MessageResponse("Contraseña actualizada correctamente"));
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
    @ResponseBody
    public ResponseEntity<?> solicitarRecuperacionContrasena(@RequestBody ForgotPasswordRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("El correo electrónico es requerido"));
            }

            String email = request.getEmail().trim();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.ok(
                        new MessageResponse("Si el correo existe, recibiras tu nueva contraseña en unos minutos"));
            }

            String nuevaContrasena = generarContrasenaAleatoria();
            enviarCorreoNuevaContrasena(user.getName(), email, nuevaContrasena);

            user.setPassword(passwordEncoder.encode(nuevaContrasena));
            userRepository.save(user);

            return ResponseEntity.ok(
                    new MessageResponse("Si el correo existe, recibiras tu nueva contraseña en unos minutos"));
        } catch (MessagingException e) {
            log.error("No se pudo enviar el correo de recuperación para: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResponse("No pudimos enviar el correo de recuperación en este momento. Intenta de nuevo más tarde."));
        } catch (Exception e) {
            log.error("Error al procesar recuperación de contraseña para: {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al procesar la solicitud. Por favor, intenta nuevamente"));
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

    // =========================
    // CLASES HELPER PARA RESPUESTAS JSON
    // =========================

    /**
     * Clase para respuestas de error
     */
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ForgotPasswordRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class UpdateProfileRequest {
        private String name;
        private String email;
        private String phone;
        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    /**
     * Clase para request de login
     */
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * Clase para respuesta de login
     */
    public static class LoginResponse {
        private Long id;
        private String name;
        private String email;
        private Long idRole;
        private String status;
        private String message;
        private String redirect;

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Long getIdRole() {
            return idRole;
        }

        public void setIdRole(Long idRole) {
            this.idRole = idRole;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRedirect() {
            return redirect;
        }

        public void setRedirect(String redirect) {
            this.redirect = redirect;
        }
    }

    /**
     * Clase para respuesta de registro
     */
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
        private Long idRole;
        private String message;

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Long getIdRole() {
            return idRole;
        }

        public void setIdRole(Long idRole) {
            this.idRole = idRole;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ProfileResponse {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String address;
        private Long idRole;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Long getIdRole() {
            return idRole;
        }

        public void setIdRole(Long idRole) {
            this.idRole = idRole;
        }
    }

    /**
     * Clase para mensajes simples de respuesta
     */
    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
