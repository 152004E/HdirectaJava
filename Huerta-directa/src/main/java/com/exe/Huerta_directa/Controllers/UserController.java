package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // Constantes para email centralizadas para evitar duplicación
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_PORT = "587";
    private static final String SENDER_EMAIL = "hdirecta@gmail.com";
    // Nota: la contraseña de aplicación idealmente debe guardarse en properties/secret manager
    private static final String SENDER_PASSWORD = "agst ebgg yakk lohu";

    public UserController(UserService userService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // Aqui irian los endpoints para manejar las solicitudes HTTP relacionadas con

    // Metodo para listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserDTO>> listarUsers() {
        return new ResponseEntity<>(userService.listarUsers(), HttpStatus.OK);
    }

    // Metodo para obtener un usuario por su id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> obtenerUserPorId(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.obtenerUserPorId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> crearUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.crearUser(userDTO), HttpStatus.CREATED);
    }

    // Metodo para actualizar un usuario
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> actualizarUser(@PathVariable("userId") Long userId, @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.actualizarUser(userId, userDTO), HttpStatus.OK);
    }

    // Metodo para eliminar un usuario por su id
    @DeleteMapping("/{userId}")
    @Transactional
    public ResponseEntity<Void> eliminarUserPorId(@PathVariable("userId") Long userId) {
        userService.eliminarUserPorId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ========== EXPORTACIÓN CON FILTROS ==========

    // Método para exportar usuarios a Excel CON FILTROS
    @GetMapping("/exportExcel")
    public void exportarExcel(
            HttpServletResponse response,
            @RequestParam(required = false) String dato,
            @RequestParam(required = false) String valor) throws IOException {

        // Obtener usuarios filtrados
        List<UserDTO> usuarios = obtenerUsuariosFiltrados(dato, valor);

        // Configurar respuesta HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "Usuarios_" + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        // Crear libro Excel
        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Usuarios");

        // Crear encabezados
        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Rol ID");

        // Si hay filtro, agregar fila informativa
        if (dato != null && valor != null && !valor.isEmpty()) {
            org.apache.poi.ss.usermodel.Row filterRow = sheet.createRow(1);
            filterRow.createCell(0).setCellValue("Filtro aplicado:");
            filterRow.createCell(1).setCellValue(dato + " = " + valor);
        }

        // Llenar datos
        int rowNum = (dato != null && valor != null && !valor.isEmpty()) ? 2 : 1;
        for (UserDTO usuario : usuarios) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(usuario.getId());
            row.createCell(1).setCellValue(usuario.getName());
            row.createCell(2).setCellValue(usuario.getEmail());
            row.createCell(3).setCellValue(usuario.getIdRole() != null ? usuario.getIdRole() : 0);
        }

        // Ajustar ancho de columnas
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        // Escribir y cerrar
        workbook.write(response.getOutputStream());
        workbook.close();
        response.getOutputStream().flush();
    }

    // Endpoint para exportar usuarios a PDF CON FILTROS
    @GetMapping("/exportPdf")
    public void exportUsersToPdf(
            HttpServletResponse response,
            @RequestParam(required = false) String dato,
            @RequestParam(required = false) String valor) throws IOException {

        // Obtener usuarios filtrados
        List<UserDTO> usuarios = obtenerUsuariosFiltrados(dato, valor);

        try {
            // Configurar la respuesta HTTP para descarga de archivo PDF
            response.setContentType("application/pdf");
            String filename = "Usuarios_" + java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setHeader("Cache-Control", "no-cache");

            // Crear documento PDF
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Título principal
            com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(
                    com.lowagie.text.FontFactory.HELVETICA_BOLD, 20, java.awt.Color.decode("#689f38"));
            com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("HUERTA DIRECTA", titleFont);
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(title);

            // Subtítulo
            com.lowagie.text.Font subtitleFont = com.lowagie.text.FontFactory.getFont(
                    com.lowagie.text.FontFactory.HELVETICA_BOLD, 14, java.awt.Color.BLACK);
            com.lowagie.text.Paragraph subtitle = new com.lowagie.text.Paragraph("Reporte de Usuarios", subtitleFont);
            subtitle.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(10);
            document.add(subtitle);

            // Información del filtro aplicado
            if (dato != null && valor != null && !valor.isEmpty()) {
                com.lowagie.text.Font filterFont = com.lowagie.text.FontFactory.getFont(
                        com.lowagie.text.FontFactory.HELVETICA_BOLD, 12, java.awt.Color.decode("#689f38"));
                com.lowagie.text.Paragraph filterInfo = new com.lowagie.text.Paragraph(
                        "Filtro aplicado: " + dato + " = \"" + valor + "\"", filterFont);
                filterInfo.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                filterInfo.setSpacingAfter(15);
                document.add(filterInfo);
            }

            // Información del reporte
            com.lowagie.text.Font infoFont = com.lowagie.text.FontFactory.getFont(
                    com.lowagie.text.FontFactory.HELVETICA, 10, java.awt.Color.GRAY);
            String currentDate = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            com.lowagie.text.Paragraph reportInfo = new com.lowagie.text.Paragraph(
                    "Fecha: " + currentDate + " | Total: " + usuarios.size() + " usuario(s)", infoFont);
            reportInfo.setAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
            reportInfo.setSpacingAfter(20);
            document.add(reportInfo);

            if (usuarios.isEmpty()) {
                // Si no hay usuarios
                com.lowagie.text.Font noDataFont = com.lowagie.text.FontFactory.getFont(
                        com.lowagie.text.FontFactory.HELVETICA, 12, java.awt.Color.RED);
                com.lowagie.text.Paragraph noData = new com.lowagie.text.Paragraph(
                        "No se encontraron usuarios con los filtros aplicados.", noDataFont);
                noData.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                noData.setSpacingBefore(50);
                document.add(noData);
            } else {
                // Crear tabla con 4 columnas
                com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(4);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                float[] columnWidths = { 1f, 3f, 4f, 2f };
                table.setWidths(columnWidths);

                // Encabezados
                com.lowagie.text.Font headerFont = com.lowagie.text.FontFactory.getFont(
                        com.lowagie.text.FontFactory.HELVETICA_BOLD, 12, java.awt.Color.WHITE);
                addTableHeaderPdf(table, "ID", headerFont);
                addTableHeaderPdf(table, "Nombre", headerFont);
                addTableHeaderPdf(table, "Email", headerFont);
                addTableHeaderPdf(table, "Rol", headerFont);

                // Datos
                com.lowagie.text.Font dataFont = com.lowagie.text.FontFactory.getFont(
                        com.lowagie.text.FontFactory.HELVETICA, 10, java.awt.Color.BLACK);
                int rowCount = 0;
                for (UserDTO usuario : usuarios) {
                    rowCount++;
                    java.awt.Color rowColor = (rowCount % 2 == 0) ? new java.awt.Color(240, 240, 240)
                            : java.awt.Color.WHITE;

                    addTableCellPdf(table, String.valueOf(usuario.getId()), dataFont, rowColor,
                            com.lowagie.text.Element.ALIGN_CENTER);
                    addTableCellPdf(table, usuario.getName() != null ? usuario.getName() : "N/A",
                            dataFont, rowColor, com.lowagie.text.Element.ALIGN_LEFT);
                    addTableCellPdf(table, usuario.getEmail() != null ? usuario.getEmail() : "N/A",
                            dataFont, rowColor, com.lowagie.text.Element.ALIGN_LEFT);

                    String roleName = obtenerNombreRol(usuario.getIdRole());
                    addTableCellPdf(table, roleName, dataFont, rowColor,
                            com.lowagie.text.Element.ALIGN_CENTER);
                }

                document.add(table);

                // Estadísticas por rol
                java.util.Map<String, Long> usersByRole = usuarios.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                                user -> obtenerNombreRol(user.getIdRole()),
                                java.util.stream.Collectors.counting()));

                if (!usersByRole.isEmpty()) {
                    document.add(new com.lowagie.text.Paragraph(" ")); // Espacio

                    com.lowagie.text.Font statsFont = com.lowagie.text.FontFactory.getFont(
                            com.lowagie.text.FontFactory.HELVETICA_BOLD, 12, java.awt.Color.BLACK);
                    com.lowagie.text.Paragraph statsTitle = new com.lowagie.text.Paragraph(
                            "Estadísticas por Rol:", statsFont);
                    statsTitle.setSpacingBefore(20);
                    document.add(statsTitle);

                    com.lowagie.text.Font statsDataFont = com.lowagie.text.FontFactory.getFont(
                            com.lowagie.text.FontFactory.HELVETICA, 10, java.awt.Color.BLACK);
                    for (java.util.Map.Entry<String, Long> entry : usersByRole.entrySet()) {
                        com.lowagie.text.Paragraph statLine = new com.lowagie.text.Paragraph(
                                "• " + entry.getKey() + ": " + entry.getValue() + " usuario(s)", statsDataFont);
                        statLine.setIndentationLeft(20);
                        document.add(statLine);
                    }
                }
            }

            // Pie de página
            com.lowagie.text.Font footerFont = com.lowagie.text.FontFactory.getFont(
                    com.lowagie.text.FontFactory.HELVETICA_OBLIQUE, 8, java.awt.Color.GRAY);
            com.lowagie.text.Paragraph footer = new com.lowagie.text.Paragraph(
                    "Reporte generado automáticamente por Huerta Directa", footerFont);
            footer.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

            document.close();
            response.getOutputStream().flush();

        } catch (Exception e) {
            // Un único manejo de errores para cualquier excepción durante la generación del PDF
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error al generar el archivo PDF: " + e.getMessage());
        }
    }

    //AQUI VAN LOS MÉTODOS DE LOGIN Y REGISTRO

    @PostMapping("/register")
    public String seveUserView(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        // 1) Si hay errores de validación (JSR-303), mejor renderizamos la vista sin
        // redirect
        // así BindingResult está disponible y Thymeleaf puede mostrar errores por
        // campo.
        if (result.hasErrors()) {
            return "login/login"; // renderiza la vista con errores (no redirect)
        }

        try {
            UserDTO usuarioCreado = userService.crearUser(userDTO);

            // Usar el repository para obtener la Entity User
            User userEntity = userRepository.findByEmail(usuarioCreado.getEmail()).orElse(null);
            if (userEntity != null) {
                session.setAttribute("user", userEntity);
            } else {
                // Si por alguna razón no se encuentra, crear un User básico del DTO
                System.err.println("⚠️ No se pudo encontrar el usuario recién creado, creando sesión básica");
                session.setAttribute("user", convertirDTOaEntity(usuarioCreado));
            }

            //Enviar correo de confirmación
            enviarCorreoConfirmacion(usuarioCreado.getName(), usuarioCreado.getEmail());

            // Si querés iniciar sesión automáticamente y redirigir:
            if (usuarioCreado.getIdRole() != null && usuarioCreado.getIdRole() == 1L) {
                redirectAttributes.addFlashAttribute("success",
                        "¡Bienvenido Administrador! Tu cuenta ha sido creada exitosamente");
                return "redirect:/DashboardAdmin";
            } else {
                redirectAttributes.addFlashAttribute("success", "¡Bienvenido! Tu cuenta ha sido creada exitosamente");
                return "redirect:/index";
            }

        } catch (DataIntegrityViolationException e) {
            // Error de llave única (email duplicado)
            redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado");
            // Reenviamos los datos del formulario para que se muestren en la vista
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la cuenta. Por favor, intente nuevamente");
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            return "redirect:/login";
        }

    }

    //MÉTODO PARA ENVIAR EL CORREO
    private void enviarCorreoConfirmacion(String nombre, String email) throws MessagingException {
        Session session = crearSesionCorreo();

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("🌱 Registro exitoso en Huerta Directa");

        // Crear el contenido HTML del correo
        String htmlContent = crearContenidoHTMLCorreo(nombre);

        // Configurar el mensaje como HTML
        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
    }

    // Método para crear el contenido HTML del correo
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
                                            🧺 ¿Qué puedes hacer ahora?
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
            """.formatted(nombre);
    }

    // Método reutilizable para crear la sesión de correo con las constantes
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

    @PostMapping("/loginUser")
    public String loginUser(@RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            model.addAttribute("userDTO", new UserDTO()); // importante para no romper el form
            return "login/login";
        }

        // Guarda al usuario en sesión
        session.setAttribute("user", user);

        // Redirige según el rol usando redirección inteligente
        if (user.getRole().getIdRole() == 1) {
            return "redirect:/DashboardAdmin";
        } else {
            return "redirect:/index";
        }
    }


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

        redirect.addFlashAttribute("success", "Administrador creado con éxito");
        return "redirect:/DashboardAdmin"; // redirección al dashboard
    }

    /**
     * Método para cerrar sesión
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destruir completamente la sesión
        return "redirect:/login";
    }

    /**
     * Método para obtener información del usuario logueado (útil para mostrar en el
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
        userDTO.setIdRole(user.getRole() != null ? user.getRole().getIdRole() : null);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // ========== MÉTODOS AUXILIARES PARA EXPORTACIÓN ==========

    private List<UserDTO> obtenerUsuariosFiltrados(String dato, String valor) {
        List<UserDTO> todosUsuarios = userService.listarUsers();

        if (dato == null || valor == null || valor.isEmpty()) {
            return todosUsuarios;
        }

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
                .collect(java.util.stream.Collectors.toList());
    }

    private boolean filtrarPorRol(UserDTO usuario, String valor) {
        if (usuario.getIdRole() == null || valor == null) {
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

        // Buscar por nombre del rol (comprobación combinada)
        if ((roleId == 1 && (valorLower.contains("admin") || valorLower.contains("administrador")))
                || (roleId == 2 && (valorLower.contains("client") || valorLower.contains("usuario")))) {
            return true;
        }

        return false;
    }

    private String obtenerNombreRol(Long idRole) {
        if (idRole == null) {
            return "Sin Rol";
        }
        return switch (idRole.intValue()) {
            case 1 -> "Administrador";
            case 2 -> "Cliente";
            default -> "Otro";
        };
    }

    private void addTableHeaderPdf(com.lowagie.text.pdf.PdfPTable table, String headerTitle,
            com.lowagie.text.Font font) {
        com.lowagie.text.pdf.PdfPCell header = new com.lowagie.text.pdf.PdfPCell();
        header.setBackgroundColor(java.awt.Color.decode("#689f38"));
        header.setBorderWidth(1);
        header.setPhrase(new com.lowagie.text.Phrase(headerTitle, font));
        header.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        header.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
        header.setPadding(8);
        table.addCell(header);
    }

    private void addTableCellPdf(com.lowagie.text.pdf.PdfPTable table, String text,
            com.lowagie.text.Font font, java.awt.Color backgroundColor,
            int alignment) {
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell();
        cell.setPhrase(new com.lowagie.text.Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(5);
        cell.setBorderWidth(1);
        table.addCell(cell);
    }

    // ========== MÉTODOS AUXILIARES PARA EXPORTACIÓN ==========

    /**
     * Método auxiliar para convertir UserDTO a User Entity
     * Se usa como respaldo si no se puede encontrar el usuario en la base de datos
     */
    private User convertirDTOaEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setCreacionDate(userDTO.getCreacionDate());

        // Crear un Role básico si es necesario
        if (userDTO.getIdRole() != null) {
            com.exe.Huerta_directa.Entity.Role role = new com.exe.Huerta_directa.Entity.Role();
            role.setIdRole(userDTO.getIdRole());
            user.setRole(role);
        }

        return user;
    }
}
