package com.exe.Huerta_directa.Controllers;
import com.exe.Huerta_directa.DTO.BulkEmailByRoleRequest;
import com.exe.Huerta_directa.DTO.BulkEmailFilteredRequest;
import com.exe.Huerta_directa.DTO.BulkEmailRequest;
import com.exe.Huerta_directa.DTO.BulkEmailResponse;
import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Entity.Role;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.UserService;
import com.exe.Huerta_directa.Service.ProductService;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
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
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
@Controller
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProductService productService;
    // Constantes para email centralizadas para evitar duplicaciÃ³n
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_PORT = "587";
    private static final String SENDER_EMAIL = "hdirecta@gmail.com";
    // Nota: la contraseÃ±a de aplicaciÃ³n idealmente debe guardarse en
    // properties/secret manager
    private static final String SENDER_PASSWORD = "agst ebgg yakk lohu";
    public UserController(UserService userService, UserRepository userRepository, ProductService productService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.productService = productService;
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
    // ========== EXPORTACIÃ“N CON FILTROS ==========
    // MÃ©todo para exportar usuarios a Excel CON FILTROS
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
        headerRow.createCell(3).setCellValue("GÃ©nero");
        headerRow.createCell(4).setCellValue("Edad");
        headerRow.createCell(5).setCellValue("Rol ID");
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
            row.createCell(3).setCellValue(obtenerGeneroTexto(usuario.getGender()));
            row.createCell(4).setCellValue(calcularEdad(usuario.getBirthDate()));
            row.createCell(5).setCellValue(usuario.getIdRole() != null ? usuario.getIdRole() : 0);
        }
        // Ajustar ancho de columnas
        for (int i = 0; i < 6; i++) {
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
            // TÃ­tulo principal
            com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(
                    com.lowagie.text.FontFactory.HELVETICA_BOLD, 20, java.awt.Color.decode("#689f38"));
            com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("HUERTA DIRECTA", titleFont);
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(title);
            // SubtÃ­tulo
            com.lowagie.text.Font subtitleFont = com.lowagie.text.FontFactory.getFont(
                    com.lowagie.text.FontFactory.HELVETICA_BOLD, 14, java.awt.Color.BLACK);
            com.lowagie.text.Paragraph subtitle = new com.lowagie.text.Paragraph("Reporte de Usuarios", subtitleFont);
            subtitle.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(10);
            document.add(subtitle);
            // InformaciÃ³n del filtro aplicado
            if (dato != null && valor != null && !valor.isEmpty()) {
                com.lowagie.text.Font filterFont = com.lowagie.text.FontFactory.getFont(
                        com.lowagie.text.FontFactory.HELVETICA_BOLD, 12, java.awt.Color.decode("#689f38"));
                com.lowagie.text.Paragraph filterInfo = new com.lowagie.text.Paragraph(
                        "Filtro aplicado: " + dato + " = \"" + valor + "\"", filterFont);
                filterInfo.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                filterInfo.setSpacingAfter(15);
                document.add(filterInfo);
            }
            // InformaciÃ³n del reporte
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
                // Crear tabla con 6 columnas
                com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(6);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                float[] columnWidths = { 1f, 3f, 4f, 2f, 2f, 2f };
                table.setWidths(columnWidths);
                // Encabezados
                com.lowagie.text.Font headerFont = com.lowagie.text.FontFactory.getFont(
                        com.lowagie.text.FontFactory.HELVETICA_BOLD, 12, java.awt.Color.WHITE);
                addTableHeaderPdf(table, "ID", headerFont);
                addTableHeaderPdf(table, "Nombre", headerFont);
                addTableHeaderPdf(table, "Email", headerFont);
                addTableHeaderPdf(table, "GÃ©nero", headerFont);
                addTableHeaderPdf(table, "Edad", headerFont);
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
                    addTableCellPdf(table, obtenerGeneroTexto(usuario.getGender()), dataFont, rowColor,
                            com.lowagie.text.Element.ALIGN_CENTER);
                    addTableCellPdf(table, String.valueOf(calcularEdad(usuario.getBirthDate())), dataFont, rowColor,
                            com.lowagie.text.Element.ALIGN_CENTER);
                    String roleName = obtenerNombreRol(usuario.getIdRole());
                    addTableCellPdf(table, roleName, dataFont, rowColor,
                            com.lowagie.text.Element.ALIGN_CENTER);
                }
                document.add(table);
                // EstadÃ­sticas por rol
                java.util.Map<String, Long> usersByRole = usuarios.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                                user -> obtenerNombreRol(user.getIdRole()),
                                java.util.stream.Collectors.counting()));
                // EstadÃ­sticas por gÃ©nero
                java.util.Map<String, Long> usersByGender = usuarios.stream()
                        .filter(u -> u.getGender() != null)
                        .collect(java.util.stream.Collectors.groupingBy(
                                user -> obtenerGeneroTexto(user.getGender()),
                                java.util.stream.Collectors.counting()));
                if (!usersByRole.isEmpty() || !usersByGender.isEmpty()) {
                    document.add(new com.lowagie.text.Paragraph(" ")); // Espacio
                    com.lowagie.text.Font statsFont = com.lowagie.text.FontFactory.getFont(
                            com.lowagie.text.FontFactory.HELVETICA_BOLD, 12, java.awt.Color.BLACK);
                    com.lowagie.text.Paragraph statsTitle = new com.lowagie.text.Paragraph(
                            "EstadÃ­sticas:", statsFont);
                    statsTitle.setSpacingBefore(20);
                    document.add(statsTitle);
                    com.lowagie.text.Font statsDataFont = com.lowagie.text.FontFactory.getFont(
                            com.lowagie.text.FontFactory.HELVETICA, 10, java.awt.Color.BLACK);
                    // EstadÃ­sticas por rol
                    if (!usersByRole.isEmpty()) {
                        com.lowagie.text.Paragraph roleTitle = new com.lowagie.text.Paragraph(
                                "Por Rol:", statsDataFont);
                        roleTitle.setSpacingBefore(10);
                        document.add(roleTitle);
                        for (java.util.Map.Entry<String, Long> entry : usersByRole.entrySet()) {
                            com.lowagie.text.Paragraph statLine = new com.lowagie.text.Paragraph(
                                    "â€¢ " + entry.getKey() + ": " + entry.getValue() + " usuario(s)", statsDataFont);
                            statLine.setIndentationLeft(20);
                            document.add(statLine);
                        }
                    }
                    // EstadÃ­sticas por gÃ©nero
                    if (!usersByGender.isEmpty()) {
                        com.lowagie.text.Paragraph genderTitle = new com.lowagie.text.Paragraph(
                                "Por GÃ©nero:", statsDataFont);
                        genderTitle.setSpacingBefore(10);
                        document.add(genderTitle);
                        for (java.util.Map.Entry<String, Long> entry : usersByGender.entrySet()) {
                            com.lowagie.text.Paragraph statLine = new com.lowagie.text.Paragraph(
                                    "â€¢ " + entry.getKey() + ": " + entry.getValue() + " usuario(s)", statsDataFont);
                            statLine.setIndentationLeft(20);
                            document.add(statLine);
                        }
                    }
                }
            }
            // Pie de pÃ¡gina
            com.lowagie.text.Font footerFont = com.lowagie.text.FontFactory.getFont(
                    com.lowagie.text.FontFactory.HELVETICA_OBLIQUE, 8, java.awt.Color.GRAY);
            com.lowagie.text.Paragraph footer = new com.lowagie.text.Paragraph(
                    "Reporte generado automÃ¡ticamente por Huerta Directa", footerFont);
            footer.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);
            document.close();
            response.getOutputStream().flush();
        } catch (Exception e) {
            // Un Ãºnico manejo de errores para cualquier excepciÃ³n durante la generaciÃ³n del
            // PDF
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error al generar el archivo PDF: " + e.getMessage());
        }
    }
    // AQUI VAN LOS MÃ‰TODOS DE LOGIN Y REGISTRO
    @Autowired
    private PasswordEncoder passwordEncoder; // o BCryptPasswordEncoder, pero mejor PasswordEncoder
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
            // Validar edad mÃ­nima (18 aÃ±os)
            if (userDTO.getBirthDate() != null) {
                LocalDate today = LocalDate.now();
                Period age = Period.between(userDTO.getBirthDate(), today);
                if (age.getYears() < 18) {
                    redirectAttributes.addFlashAttribute("error", "Debes ser mayor de 18 aÃ±os para registrarte");
                    redirectAttributes.addFlashAttribute("userDTO", userDTO);
                    return "redirect:/login";
                }
            }
            // âŒ NO hashear aquÃ­: quitar la siguiente lÃ­nea si existe en tu controlador
            // userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
                redirectAttributes.addFlashAttribute("success", "Â¡Bienvenido Administrador!");
                return "redirect:/DashboardAdmin";
            } else {
                redirectAttributes.addFlashAttribute("success", "Â¡Bienvenido!");
                return "redirect:/index";
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "El correo electrÃ³nico ya estÃ¡ registrado");
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al crear la cuenta.");
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            return "redirect:/login";
        }
    }
    // MÃ‰TODO PARA ENVIAR EL CORREO
    private void enviarCorreoConfirmacion(String nombre, String email) throws MessagingException {
        Session session = crearSesionCorreo();
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("ðŸŒ± Registro exitoso en Huerta Directa");
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
                                            ðŸŒ± Huerta Directa
                                        </h1>
                                        <p style="color: #ffffff; margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;">
                                            Conectando el campo con tu mesa
                                        </p>
                                    </div>
                                    <!-- Contenido principal -->
                                    <div style="padding: 40px 30px;">
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <div style="background-color: #e8f5e8; border-radius: 50px; width: 80px; height: 80px; margin: 0 auto 20px auto; display: flex; align-items: center; justify-content: center; font-size: 35px;">
                                                âœ…
                                            </div>
                                            <h2 style="color: #2e7d32; margin: 0; font-size: 24px; font-weight: bold;">
                                                Â¡Registro Exitoso!
                                            </h2>
                                        </div>
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <p style="color: #333333; font-size: 18px; line-height: 1.6; margin-bottom: 15px;">
                                                Â¡Hola <strong style="color: #689f38;">%s</strong>! ðŸ‘‹
                                            </p>
                                            <p style="color: #666666; font-size: 16px; line-height: 1.6; margin-bottom: 20px;">
                                                Tu cuenta en <strong>Huerta Directa</strong> ha sido creada exitosamente.
                                                Ahora formas parte de nuestra comunidad que conecta directamente a productores
                                                campesinos con consumidores como tÃº.
                                            </p>
                                        </div>
                                        <!-- Beneficios -->
                                        <div style="background-color: #f8f9fa; border-radius: 8px; padding: 25px; margin-bottom: 30px;">
                                            <h3 style="color: #2e7d32; margin: 0 0 20px 0; font-size: 18px; text-align: center;">
                                                ðŸ§º Â¿QuÃ© puedes hacer ahora?
                                            </h3>
                                            <div style="text-align: left;">
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    ðŸ¥• <strong>Explorar productos frescos</strong> directamente de la huerta
                                                </p>
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    ðŸšš <strong>Realizar pedidos</strong> con entrega a domicilio
                                                </p>
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    ðŸ‘¨â€ðŸŒ¾ <strong>Conocer a los productores</strong> detrÃ¡s de tus alimentos
                                                </p>
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    ðŸ’š <strong>Apoyar la agricultura local</strong> y sostenible
                                                </p>
                                            </div>
                                        </div>
                                        <!-- BotÃ³n de acciÃ³n -->
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <a href="#" style="display: inline-block; background: linear-gradient(135deg, #689f38 0%%, #8bc34a 100%%); color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 25px; font-weight: bold; font-size: 16px; box-shadow: 0 4px 15px rgba(104, 159, 56, 0.3); transition: all 0.3s ease;">
                                                ðŸŒŸ Comenzar a Explorar
                                            </a>
                                        </div>
                                        <!-- Mensaje de agradecimiento -->
                                        <div style="text-align: center; border-top: 2px solid #e8f5e8; padding-top: 25px;">
                                            <p style="color: #666666; font-size: 14px; line-height: 1.5; margin: 0;">
                                                Gracias por unirte a nuestra misiÃ³n de acercar el campo a tu mesa.<br>
                                                <strong style="color: #689f38;">Â¡Juntos construimos un futuro mÃ¡s verde! ðŸŒ</strong>
                                            </p>
                                        </div>
                                    </div>
                                    <!-- Footer -->
                                    <div style="background-color: #2e7d32; padding: 25px 30px; text-align: center;">
                                        <p style="color: #ffffff; margin: 0 0 10px 0; font-size: 16px; font-weight: bold;">
                                            El equipo de Huerta Directa ðŸŒ±
                                        </p>
                                        <p style="color: #c8e6c9; margin: 0; font-size: 12px;">
                                            Este correo fue enviado automÃ¡ticamente. Por favor, no respondas a este mensaje.
                                        </p>
                                        <div style="margin-top: 15px;">
                                            <span style="color: #c8e6c9; font-size: 12px;">
                                                Â© 2024 Huerta Directa - Todos los derechos reservados
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
    // MÃ©todo reutilizable para crear la sesiÃ³n de correo con las constantes
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
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        // Buscar el usuario por correo
        User user = userRepository.findByEmail(email).orElse(null);
        // âš ï¸ Validar si el usuario existe y si la contraseÃ±a coincide
        if (user == null) {
            model.addAttribute("error", "Correo o contraseÃ±a incorrectos");
            model.addAttribute("userDTO", new UserDTO());
            return "login/login";
        }
        // Verificar con BCrypt si la contraseÃ±a plana coincide con el hash
        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Correo o contraseÃ±a incorrectos");
            model.addAttribute("userDTO", new UserDTO());
            return "login/login";
        }
        // Guardar usuario en la sesiÃ³n
        session.setAttribute("user", user);
        // Redirigir segÃºn el rol
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
        redirect.addFlashAttribute("success", "Administrador creado con Ã©xito");
        return "redirect:/DashboardAdmin"; // redirecciÃ³n al dashboard
    }
    /**
     * MÃ©todo para cerrar sesiÃ³n
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destruir completamente la sesiÃ³n
        return "redirect:/login";
    }
    /**
     * MÃ©todo para obtener informaciÃ³n del usuario logueado (Ãºtil para mostrar en el
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
    // ========== MÃ‰TODOS AUXILIARES PARA EXPORTACIÃ“N ==========
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
                        case "gender":
                            return usuario.getGender() != null &&
                                    obtenerGeneroTexto(usuario.getGender()).toLowerCase().contains(valor.toLowerCase());
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
            // No es un nÃºmero, continuar con bÃºsqueda por nombre
        }
        // Buscar por nombre del rol (comprobaciÃ³n combinada)
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
    // MÃ©todo para obtener texto del gÃ©nero
    private String obtenerGeneroTexto(String gender) {
        if (gender == null) {
            return "No especificado";
        }
        return switch (gender) {
            case "M" -> "Masculino";
            case "F" -> "Femenino";
            case "O" -> "Otro";
            default -> "No especificado";
        };
    }
    // MÃ©todo para calcular edad
    private int calcularEdad(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    private void addTableHeaderPdf(PdfPTable table, String headerTitle,
            com.lowagie.text.Font font) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(java.awt.Color.decode("#689f38"));
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(headerTitle, font));
        header.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        header.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
        header.setPadding(8);
        table.addCell(header);
    }
    private void addTableCellPdf(PdfPTable table, String text,
            com.lowagie.text.Font font, java.awt.Color backgroundColor,
            int alignment) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(5);
        cell.setBorderWidth(1);
        table.addCell(cell);
    }
    // ========== MÃ‰TODOS AUXILIARES PARA EXPORTACIÃ“N ==========
    /**
     * MÃ©todo auxiliar para convertir UserDTO a User Entity
     * Se usa como respaldo si no se puede encontrar el usuario en la base de datos
     */
    private User convertirDTOaEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setCreacionDate(userDTO.getCreacionDate());
        // Crear un Role bÃ¡sico si es necesario
        if (userDTO.getIdRole() != null) {
            com.exe.Huerta_directa.Entity.Role role = new Role();
            role.setIdRole(userDTO.getIdRole());
            user.setRole(role);
        }
        return user;
    }
    // ========== ENVÃO DE CORREOS MASIVOS ==========
    /**
     * Endpoint para enviar correo masivo a todos los usuarios
     */
    @PostMapping("/send-bulk-email")
    @ResponseBody
    public ResponseEntity<BulkEmailResponse> enviarCorreoMasivo(@RequestBody BulkEmailRequest request) {
        try {
            List<User> users = userRepository.findAll().stream()
                    .filter(user -> user.getEmail() != null && !user.getEmail().isEmpty())
                    .collect(Collectors.toList());
            if (users.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BulkEmailResponse(0, 0, "No hay usuarios con emails vÃ¡lidos"));
            }
            // EnvÃ­o masivo real sin personalizaciÃ³n individual
            try {
                enviarCorreoMasivoRapido(users, request.getSubject(), request.getBody());
                BulkEmailResponse response = new BulkEmailResponse(users.size(), 0,
                        "Correo enviado masivamente a " + users.size() + " usuarios");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new BulkEmailResponse(0, users.size(), "Error en el envÃ­o masivo: " + e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BulkEmailResponse(0, 0, "Error en el envÃ­o masivo: " + e.getMessage()));
        }
    }
    /**
     * Endpoint para enviar correo masivo filtrado por IDs o emails
     */
    @PostMapping("/send-bulk-email-filtered")
    @ResponseBody
    public ResponseEntity<BulkEmailResponse> enviarCorreoMasivoFiltrado(@RequestBody BulkEmailFilteredRequest request) {
        try {
            List<User> users = new java.util.ArrayList<>();
            if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
                users = userRepository.findAllById(request.getUserIds());
            } else if (request.getEmails() != null && !request.getEmails().isEmpty()) {
                users = userRepository.findByEmailIn(request.getEmails());
            } else {
                return ResponseEntity.badRequest()
                        .body(new BulkEmailResponse(0, 0, "Debe proporcionar IDs de usuarios o emails"));
            }
            users = users.stream()
                    .filter(user -> user.getEmail() != null && !user.getEmail().isEmpty())
                    .collect(Collectors.toList());
            if (users.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BulkEmailResponse(0, 0, "No hay usuarios vÃ¡lidos para enviar"));
            }
            // EnvÃ­o masivo real sin personalizaciÃ³n individual
            try {
                enviarCorreoMasivoRapido(users, request.getSubject(), request.getBody());
                BulkEmailResponse response = new BulkEmailResponse(users.size(), 0,
                        "Correo enviado masivamente a " + users.size() + " usuarios filtrados");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new BulkEmailResponse(0, users.size(), "Error en el envÃ­o masivo: " + e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BulkEmailResponse(0, 0, "Error: " + e.getMessage()));
        }
    }
    /**
     * Endpoint para enviar correo masivo por rol
     */
    @PostMapping("/send-bulk-email-by-role")
    @ResponseBody
    public ResponseEntity<BulkEmailResponse> enviarCorreoMasivoPorRol(@RequestBody BulkEmailByRoleRequest request) {
        try {
            List<User> users = userRepository.findAll().stream()
                    .filter(user -> user.getEmail() != null && !user.getEmail().isEmpty())
                    .filter(user -> user.getRole() != null && user.getRole().getIdRole().equals(request.getIdRole()))
                    .collect(java.util.stream.Collectors.toList());
            if (users.isEmpty()) {
                String roleName = request.getIdRole() == 1 ? "Administradores" : "Clientes";
                return ResponseEntity.badRequest()
                        .body(new BulkEmailResponse(0, 0, "No hay " + roleName + " con emails vÃ¡lidos"));
            }
            // EnvÃ­o masivo real sin personalizaciÃ³n individual
            try {
                enviarCorreoMasivoRapido(users, request.getSubject(), request.getBody());
                String roleName = request.getIdRole() == 1 ? "administradores" : "clientes";
                BulkEmailResponse response = new BulkEmailResponse(users.size(), 0,
                        "Correo enviado masivamente a " + users.size() + " " + roleName);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new BulkEmailResponse(0, users.size(), "Error en el envÃ­o masivo: " + e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BulkEmailResponse(0, 0, "Error: " + e.getMessage()));
        }
    }
    /**
     * MÃ©todo para envÃ­o masivo rÃ¡pido - EnvÃ­a a todos los destinatarios en una sola
     * operaciÃ³n
     */
    private void enviarCorreoMasivoRapido(List<User> users, String asunto, String cuerpo) throws MessagingException {
        Session session = crearSesionCorreo();
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        // Agregar todos los destinatarios de una vez usando BCC para privacidad
        InternetAddress[] destinatarios = new InternetAddress[users.size()];
        for (int i = 0; i < users.size(); i++) {
            destinatarios[i] = new InternetAddress(users.get(i).getEmail());
        }
        // Usar BCC para envÃ­o masivo manteniendo privacidad de emails
        message.setRecipients(Message.RecipientType.BCC, destinatarios);
        message.setSubject(asunto);
        // Configurar el contenido
        if (cuerpo.trim().startsWith("<!DOCTYPE") || cuerpo.trim().startsWith("<html")) {
            message.setContent(cuerpo, "text/html; charset=utf-8");
        } else {
            message.setText(cuerpo, "utf-8");
        }
        // Enviar el correo masivo en una sola operaciÃ³n
        Transport.send(message);
    }
    /**
     * MÃ©todo privado para enviar correo personalizado
     */
    //  lo comente por que me daba error y sÃ© que no se usa/////
    // private void enviarCorreoPersonalizado(String destinatario, String asunto, String cuerpo)
    //         throws MessagingException {
    //     Session session = crearSesionCorreo();
    //     MimeMessage message = new MimeMessage(session);
    //     message.setFrom(new InternetAddress(SENDER_EMAIL));
    //     message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
    //     message.setSubject(asunto);
    //     // Detectar si el cuerpo es HTML o texto plano
    //     if (cuerpo.trim().startsWith("<!DOCTYPE") || cuerpo.trim().startsWith("<html")) {
    //         message.setContent(cuerpo, "text/html; charset=utf-8");
    //     } else {
    //         message.setText(cuerpo, "utf-8");
    //     }
    //     Transport.send(message);
    // }
    // ========== RECUPERACIÃ“N DE CONTRASEÃ‘A ==========
    /**
     * Endpoint para solicitar recuperaciÃ³n de contraseÃ±a - VersiÃ³n simple como el
     * registro
     */
    @PostMapping("/forgot-password")
    public String solicitarRecuperacionContrasena(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                // Por seguridad, no revelamos si el email existe o no, pero mostramos el mismo
                // mensaje
                redirectAttributes.addFlashAttribute("success",
                        "Si el correo existe, recibirÃ¡s tu nueva contraseÃ±a en unos minutos");
                return "redirect:/forgot-password";
            }
            // Generar nueva contraseÃ±a aleatoria
            String nuevaContrasena = generarContrasenaAleatoria();
            // Se hashea la nueva contraseÃ±a antes de guardarla
            user.setPassword(passwordEncoder.encode(nuevaContrasena));
            userRepository.save(user);
            // Enviar correo con la nueva contraseÃ±a
            enviarCorreoNuevaContrasena(user.getName(), email, nuevaContrasena);
            redirectAttributes.addFlashAttribute("success",
                    "Si el correo existe, recibirÃ¡s tu nueva contraseÃ±a en unos minutos");
            return "redirect:/forgot-password";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al procesar la solicitud. Por favor, intenta nuevamente");
            return "redirect:/forgot-password";
        }
    }
    /**
     * MÃ©todo para generar una contraseÃ±a aleatoria segura
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
     * MÃ©todo para enviar correo con la nueva contraseÃ±a
     */
    private void enviarCorreoNuevaContrasena(String nombre, String email, String nuevaContrasena)
            throws MessagingException {
        Session session = crearSesionCorreo();
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("ðŸ”‘ Tu nueva contraseÃ±a - Huerta Directa");
        String htmlContent = crearContenidoHTMLNuevaContrasena(nombre, nuevaContrasena);
        message.setContent(htmlContent, "text/html; charset=utf-8");
        Transport.send(message);
    }
    /**
     * MÃ©todo para crear el contenido HTML del correo con la nueva contraseÃ±a
     */
    private String crearContenidoHTMLNuevaContrasena(String nombre, String nuevaContrasena) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Nueva ContraseÃ±a - Huerta Directa</title>
                </head>
                <body style="margin: 0; padding: 0; font-family: 'Arial', sans-serif; background-color: #f4f4f4;">
                    <table role="presentation" style="width: 100%%; border-collapse: collapse;">
                        <tr>
                            <td style="padding: 0;">
                                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
                                    <!-- Header -->
                                    <div style="background: linear-gradient(135deg, #689f38 0%%, #8bc34a 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">
                                            ðŸŒ± Huerta Directa
                                        </h1>
                                        <p style="color: #ffffff; margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;">
                                            Nueva ContraseÃ±a Generada
                                        </p>
                                    </div>
                                    <!-- Contenido principal -->
                                    <div style="padding: 40px 30px;">
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <div style="background-color: #e8f5e8; border-radius: 50px; width: 80px; height: 80px; margin: 0 auto 20px auto; display: flex; align-items: center; justify-content: center; font-size: 35px;">
                                                ðŸ”‘
                                            </div>
                                            <h2 style="color: #2e7d32; margin: 0; font-size: 24px; font-weight: bold;">
                                                Â¡Nueva ContraseÃ±a Lista!
                                            </h2>
                                        </div>
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <p style="color: #333333; font-size: 18px; line-height: 1.6; margin-bottom: 15px;">
                                                Â¡Hola <strong style="color: #689f38;">%s</strong>! ðŸ‘‹
                                            </p>
                                            <p style="color: #666666; font-size: 16px; line-height: 1.6; margin-bottom: 20px;">
                                                Hemos generado una nueva contraseÃ±a para tu cuenta en <strong>Huerta Directa</strong>.
                                                Ya puedes iniciar sesiÃ³n con esta nueva contraseÃ±a.
                                            </p>
                                        </div>
                                        <!-- Nueva contraseÃ±a -->
                                        <div style="background-color: #f8f9fa; border: 2px dashed #8dc84b; border-radius: 15px; padding: 25px; margin-bottom: 30px; text-align: center;">
                                            <h3 style="color: #2e7d32; margin: 0 0 15px 0; font-size: 18px;">
                                                ðŸ” Tu Nueva ContraseÃ±a
                                            </h3>
                                            <div style="background-color: #ffffff; border: 2px solid #8dc84b; border-radius: 10px; padding: 15px; margin: 10px 0;">
                                                <span style="font-family: 'Courier New', monospace; font-size: 24px; font-weight: bold; color: #2e7d32; letter-spacing: 2px;">
                                                    %s
                                                </span>
                                            </div>
                                            <p style="color: #666666; font-size: 12px; margin: 10px 0 0 0;">
                                                Copia esta contraseÃ±a exactamente como aparece
                                            </p>
                                        </div>
                                        <!-- Instrucciones -->
                                        <div style="background-color: #fff3e0; border-radius: 8px; padding: 20px; margin-bottom: 30px;">
                                            <h3 style="color: #f57c00; margin: 0 0 15px 0; font-size: 16px; text-align: center;">
                                                ðŸ“‹ PrÃ³ximos Pasos
                                            </h3>
                                            <div style="text-align: left;">
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    1ï¸âƒ£ Ve a la pÃ¡gina de inicio de sesiÃ³n
                                                </p>
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    2ï¸âƒ£ Usa tu email y esta nueva contraseÃ±a
                                                </p>
                                                <p style="color: #555555; margin: 8px 0; font-size: 14px;">
                                                    3ï¸âƒ£ Â¡Recomendamos cambiarla por una personalizada!
                                                </p>
                                            </div>
                                        </div>
                                        <!-- BotÃ³n de acciÃ³n -->
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <a href="http://localhost:8080/login" style="display: inline-block; background: linear-gradient(135deg, #689f38 0%%, #8bc34a 100%%); color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 25px; font-weight: bold; font-size: 16px; box-shadow: 0 4px 15px rgba(104, 159, 56, 0.3);">
                                                ðŸš€ Iniciar SesiÃ³n Ahora
                                            </a>
                                        </div>
                                        <!-- Mensaje de seguridad -->
                                        <div style="text-align: center; border-top: 2px solid #e8f5e8; padding-top: 25px;">
                                            <p style="color: #666666; font-size: 14px; line-height: 1.5; margin: 0;">
                                                Si no solicitaste este cambio, contacta inmediatamente con soporte.<br>
                                                <strong style="color: #689f38;">Tu cuenta estÃ¡ segura con nosotros ðŸ›¡ï¸</strong>
                                            </p>
                                        </div>
                                    </div>
                                    <!-- Footer -->
                                    <div style="background-color: #2e7d32; padding: 25px 30px; text-align: center;">
                                        <p style="color: #ffffff; margin: 0 0 10px 0; font-size: 16px; font-weight: bold;">
                                            El equipo de Huerta Directa ðŸŒ±
                                        </p>
                                        <p style="color: #c8e6c9; margin: 0; font-size: 12px;">
                                            Este correo fue enviado automÃ¡ticamente. Por favor, no respondas a este mensaje.
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
    // ========== CARGA DE DATOS DESDE ARCHIVO ==========
    /**
     * Endpoint para cargar datos desde archivo CSV o Excel
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> cargarDatosDesdeArchivo(@RequestParam("archivo") MultipartFile archivo) {
        try {
            // Validar que se enviÃ³ un archivo
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(java.util.Map.of(
                                "success", false,
                                "message", "No se ha seleccionado ningÃºn archivo"));
            }
            // Validar tipo de archivo
            String nombreArchivo = archivo.getOriginalFilename();
            if (nombreArchivo == null || (!nombreArchivo.endsWith(".csv") &&
                    !nombreArchivo.endsWith(".xlsx") && !nombreArchivo.endsWith(".xls"))) {
                return ResponseEntity.badRequest()
                        .body(java.util.Map.of(
                                "success", false,
                                "message", "Formato de archivo no soportado. Use CSV o Excel (.xlsx, .xls)"));
            }
            List<UserDTO> usuariosCargados = new ArrayList<>();
            int usuariosCreados = 0;
            int usuariosDuplicados = 0;
            List<String> errores = new ArrayList<>();
            if (nombreArchivo.endsWith(".csv")) {
                usuariosCargados = procesarArchivoCSV(archivo.getInputStream());
            } else {
                usuariosCargados = procesarArchivoExcel(archivo.getInputStream());
            }
            // Procesar cada usuario del archivo
            for (int i = 0; i < usuariosCargados.size(); i++) {
                UserDTO usuario = usuariosCargados.get(i);
                try {
                    // Validar datos bÃ¡sicos
                    if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                        errores.add("Fila " + (i + 2) + ": Email requerido");
                        continue;
                    }
                    if (usuario.getName() == null || usuario.getName().trim().isEmpty()) {
                        errores.add("Fila " + (i + 2) + ": Nombre requerido");
                        continue;
                    }
                    // Verificar si el usuario ya existe
                    if (userRepository.findByEmail(usuario.getEmail()).isPresent()) {
                        usuariosDuplicados++;
                        continue;
                    }
                    // Asignar valores por defecto si no estÃ¡n presentes
                    if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                        usuario.setPassword("123456"); // ContraseÃ±a por defecto
                    }
                    if (usuario.getIdRole() == null) {
                        usuario.setIdRole(2L); // Rol cliente por defecto
                    }
                    // Crear el usuario
                    userService.crearUser(usuario);
                    usuariosCreados++;
                } catch (DataIntegrityViolationException e) {
                    usuariosDuplicados++;
                } catch (Exception e) {
                    errores.add("Fila " + (i + 2) + ": " + e.getMessage());
                }
            }
            // Preparar respuesta
            java.util.Map<String, Object> respuesta = new java.util.HashMap<>();
            respuesta.put("success", true);
            respuesta.put("message", "Procesamiento completado");
            respuesta.put("usuariosCreados", usuariosCreados);
            respuesta.put("usuariosDuplicados", usuariosDuplicados);
            respuesta.put("totalProcesados", usuariosCargados.size());
            respuesta.put("errores", errores);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of(
                            "success", false,
                            "message", "Error al procesar el archivo: " + e.getMessage()));
        }
    }
    /**
     * Procesar archivo CSV
     */
    private List<UserDTO> procesarArchivoCSV(InputStream inputStream) throws IOException {
        List<UserDTO> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false; // Saltar encabezados
                    continue;
                }
                String[] campos = linea.split(",");
                if (campos.length >= 2) // Al menos nombre y email
                {
                    UserDTO usuario = new UserDTO();
                    usuario.setName(campos[0].trim());
                    usuario.setEmail(campos[1].trim());
                    // Campos opcionales
                    if (campos.length > 2 && !campos[2].trim().isEmpty()) {
                        usuario.setPassword(campos[2].trim());
                    }
                    if (campos.length > 3 && !campos[3].trim().isEmpty()) {
                        try {
                            usuario.setIdRole(Long.parseLong(campos[3].trim()));
                        } catch (NumberFormatException e) {
                            // Usar rol por defecto si no es vÃ¡lido
                        }
                    }
                    usuarios.add(usuario);
                }
            }
        }
        return usuarios;
    }
    /**
     * Procesar archivo Excel
     */
    private List<UserDTO> procesarArchivoExcel(InputStream inputStream) throws IOException {
        List<UserDTO> usuarios = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Primera hoja
            boolean primeraFila = true;
            for (Row fila : sheet) {
                if (primeraFila) {
                    primeraFila = false; // Saltar encabezados
                    continue;
                }
                if (fila.getPhysicalNumberOfCells() >= 2) {
                    UserDTO usuario = new UserDTO();
                    // Nombre (columna A)
                    Cell celdaNombre = fila.getCell(0);
                    if (celdaNombre != null) {
                        usuario.setName(obtenerValorCelda(celdaNombre));
                    }
                    // Email (columna B)
                    Cell celdaEmail = fila.getCell(1);
                    if (celdaEmail != null) {
                        usuario.setEmail(obtenerValorCelda(celdaEmail));
                    }
                    // ContraseÃ±a (columna C) - opcional
                    Cell celdaPassword = fila.getCell(2);
                    if (celdaPassword != null && !obtenerValorCelda(celdaPassword).trim().isEmpty()) {
                        usuario.setPassword(obtenerValorCelda(celdaPassword));
                    }
                    // Rol (columna D) - opcional
                    Cell celdaRol = fila.getCell(3);
                    if (celdaRol != null) {
                        try {
                            String valorRol = obtenerValorCelda(celdaRol);
                            if (!valorRol.trim().isEmpty()) {
                                usuario.setIdRole(Long.parseLong(valorRol));
                            }
                        } catch (NumberFormatException e) {
                            // Usar rol por defecto si no es vÃ¡lido
                        }
                    }
                    if (usuario.getName() != null && !usuario.getName().trim().isEmpty() &&
                            usuario.getEmail() != null && !usuario.getEmail().trim().isEmpty()) {
                        usuarios.add(usuario);
                    }
                }
            }
        }
        return usuarios;
    }
    /**
     * Obtener valor de celda como String
     */
    private String obtenerValorCelda(Cell celda) {
        if (celda == null) {
            return "";
        }
        switch (celda.getCellType()) {
            case STRING:
                return celda.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(celda)) {
                    return celda.getDateCellValue().toString();
                } else {
                    // Para nÃºmeros enteros, quitar decimales
                    double numero = celda.getNumericCellValue();
                    if (numero == (long) numero) {
                        return String.valueOf((long) numero);
                    } else {
                        return String.valueOf(numero);
                    }
                }
            case BOOLEAN:
                return String.valueOf(celda.getBooleanCellValue());
            case FORMULA:
                return celda.getCellFormula();
            default:
                return "";
        }
    }
    // ========== CARGA MASIVA DE PRODUCTOS ==========
    /**
     * Endpoint para cargar productos masivamente desde archivo CSV o Excel
     */
    @PostMapping("/upload-products")
    @ResponseBody
    public ResponseEntity<?> cargarProductosDesdeArchivo
    (@RequestParam("archivo")
     MultipartFile archivo,
     HttpSession session) {
        try {
            // OBTENER USUARIO DE LA SESIÃ“N
            User userSession = (User) session.getAttribute("user");
            if (userSession == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "success", false,
                                "message", "Sesion expirada. Debe iniciar sesion para cargar productos"));
            }
            Long currentUserId = userSession.getId();
            // Validar que se enviÃ³ un archivo
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "No se ha seleccionado ningÃºn archivo"));
            }
            // Validar tipo de archivo
            String nombreArchivo = archivo.getOriginalFilename();
            if (nombreArchivo == null || (!nombreArchivo.endsWith(".csv") &&
                    !nombreArchivo.endsWith(".xlsx") && !nombreArchivo.endsWith(".xls"))) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "Formato de archivo no soportado. Use CSV o Excel (.xlsx, .xls)"));
            }
            List<ProductDTO> productosCargados = new ArrayList<>();
            int productosCreados = 0;
            int productosDuplicados = 0;
            List<String> errores = new ArrayList<>();
            if (nombreArchivo.endsWith(".csv")) {
                productosCargados = procesarArchivoProductosCSV(archivo.getInputStream());
            } else {
                productosCargados = procesarArchivoProductosExcel(archivo.getInputStream());
            }
            // Procesar cada producto del archivo
            for (int i = 0; i < productosCargados.size(); i++) {
                ProductDTO producto = productosCargados.get(i);
                try {
                    // Validar datos bÃ¡sicos
                    if (producto.getNameProduct() == null || producto.getNameProduct().trim().isEmpty()) {
                        errores.add("Fila " + (i + 2) + ": Nombre del producto requerido");
                        continue;
                    }
                    if (producto.getPrice() == null || producto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                        errores.add("Fila " + (i + 2) + ": Precio vÃ¡lido requerido");
                        continue;
                    }
                    if (producto.getCategory() == null || producto.getCategory().trim().isEmpty()) {
                        errores.add("Fila " + (i + 2) + ": CategorÃ­a requerida");
                        continue;
                    }
                    // Asignar valores por defecto si no estÃ¡n presentes
                    if (producto.getUnit() == null || producto.getUnit().trim().isEmpty()) {
                        producto.setUnit("Unidad");
                    }
                    if (producto.getImageProduct() == null || producto.getImageProduct().trim().isEmpty()) {
                        producto.setImageProduct("default-product.png");
                    }
                    if (producto.getDescriptionProduct() == null || producto.getDescriptionProduct().trim().isEmpty()) {
                        producto.setDescriptionProduct("Producto sin descripciÃ³n");
                    }
                    if (producto.getPublicationDate() == null) {
                        producto.setPublicationDate(LocalDate.now());
                    }
                    // ASIGNAR USUARIO ACTUAL DE LA SESIÃ“N
                    if (producto.getUserId() == null) {
                        producto.setUserId(currentUserId);
                    }
                    // Verificar si el producto ya existe para este usuario
                    boolean existe = verificarProductoExistente(
                            producto.getNameProduct().trim(),
                            producto.getCategory().trim(),
                            currentUserId
                    );
                    if (existe) {
                        productosDuplicados++;
                        continue;
                    }
                    // Crear el producto usando el servicio
                    crearProductoDesdeDTO(producto);
                    productosCreados++;
                } catch (Exception e) {
                    errores.add("Fila " + (i + 2) + ": " + e.getMessage());
                }
            }
            // Preparar respuesta
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", true);
            respuesta.put("message", "Procesamiento de productos completado");
            respuesta.put("productosCreados", productosCreados);
            respuesta.put("productosDuplicados", productosDuplicados);
            respuesta.put("totalProcesados", productosCargados.size());
            respuesta.put("errores", errores);
            respuesta.put("needsRefresh", productosCreados > 0);
            respuesta.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Error al procesar el archivo: " + e.getMessage()));
        }
    }
    /**
     * Procesar archivo CSV de productos
     */
    private List<ProductDTO> procesarArchivoProductosCSV(InputStream inputStream) throws IOException {
        List<ProductDTO> productos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false; // Saltar encabezados
                    continue;
                }
                String[] campos = linea.split(",");
                if (campos.length >= 6) {
                    ProductDTO producto = new ProductDTO();
                    producto.setNameProduct(campos[0].trim());
                    try {
                        producto.setPrice(new BigDecimal(campos[1].trim()));
                    } catch (NumberFormatException e) {
                        continue; // Saltar fila con precio inválido
                    }
                    producto.setCategory(campos[2].trim());
                    producto.setUnit(campos[3].trim());
                    producto.setDescriptionProduct(campos[4].trim());
                    producto.setImageProduct(campos[5].trim());
                    producto.setPublicationDate(LocalDate.now());
                    productos.add(producto);
                }
            }
        }
        return productos;
    }
    /**
     * Procesar archivo Excel de productos
     */
    private List<ProductDTO> procesarArchivoProductosExcel(InputStream inputStream) throws IOException {
        List<ProductDTO> productos = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean primeraFila = true;
            for (Row row : sheet) {
                if (primeraFila) {
                    primeraFila = false; // Saltar encabezados
                    continue;
                }
                if (row.getPhysicalNumberOfCells() >= 6) {
                    ProductDTO producto = new ProductDTO();
                    Cell nombreCell = row.getCell(0);
                    if (nombreCell != null) {
                        producto.setNameProduct(obtenerValorCelda(nombreCell));
                    }
                    Cell precioCell = row.getCell(1);
                    if (precioCell != null) {
                        try {
                            String valorPrecio = obtenerValorCelda(precioCell);
                            if (!valorPrecio.trim().isEmpty()) {
                                producto.setPrice(new BigDecimal(valorPrecio));
                            }
                        } catch (NumberFormatException e) {
                            continue; // Saltar fila con precio invÃ¡lido
                        }
                    }
                    Cell categoriaCell = row.getCell(2);
                    if (categoriaCell != null) {
                        producto.setCategory(obtenerValorCelda(categoriaCell));
                    }
                    Cell unidadCell = row.getCell(3);
                    if (unidadCell != null) {
                        producto.setUnit(obtenerValorCelda(unidadCell));
                    }
                    Cell descripcionCell = row.getCell(4);
                    if (descripcionCell != null) {
                        producto.setDescriptionProduct(obtenerValorCelda(descripcionCell));
                    }
                    Cell imagenCell = row.getCell(5);
                    if (imagenCell != null) {
                        producto.setImageProduct(obtenerValorCelda(imagenCell));
                    }
                    producto.setPublicationDate(LocalDate.now());
                    // Validar que los campos obligatorios no estén vacíos
                    if (producto.getNameProduct() != null && !producto.getNameProduct().trim().isEmpty() &&
                            producto.getPrice() != null &&
                            producto.getCategory() != null && !producto.getCategory().trim().isEmpty()) {
                        productos.add(producto);
                    }
                }
            }
        }
        return productos;
    }

    /**
     * Verificar si un producto ya existe por nombre y categorÃ­a
     */
    private boolean verificarProductoExistente(String nombre, String categoria, Long userId) {
        try {
            return productService.existeProductoPorUsuario(nombre, categoria, userId);
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Crear producto desde DTO usando ProductService
     */
    private void crearProductoDesdeDTO(ProductDTO producto) {
        try {
            // Usar ProductService para crear el producto en la base de datos
            productService.crearProduct(producto, producto.getUserId());
        } catch (Exception e) {
            throw e; // Re-lanzar la excepciÃ³n para que sea manejada en el bucle principal
        }
    }
    @GetMapping("/products/refresh")
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> obtenerProductosActualizados() {
        try {
            List<ProductDTO> productos = productService.listarProducts();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
    @GetMapping("/admin/migrate-passwords")
    public String migratePasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            // Solo hashear si NO estÃ¡ hasheada (BCrypt empieza con $2a$)
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                String hashedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);
                userRepository.save(user);
            }
        }
        return "redirect:/DashboardAdmin";
    }
    @PostMapping("/actualizarDatos")
    public String actualizarDatos(
            @RequestParam String name,
            @RequestParam String address,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no autenticado.");
                return "redirect:/login";
            }
            User user = userRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
            user.setName(name);
            user.setAddress(address);
            userRepository.save(user);
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("success", "InformaciÃ³n actualizada exitosamente.");
            return "redirect:/actualizacionUsuario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la informaciÃ³n");
            return "redirect:/actualizacionUsuario";
        }
    }
    @PostMapping("/ActualizarContacto")
    public String actualizarContacto(
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                redirectAttributes.addFlashAttribute("error", "Sesion expirada");
                return "redirect:/login";
            }
            //Validar telefono
            if (phone != null && !phone.trim().isEmpty()) {
                //Esto eliminar espacion y caracteres no numericos
                String phoneClean = phone.replaceAll("[^0-9]", "");
                if (phoneClean.length() != 10){
                    redirectAttributes.addFlashAttribute("error", "El nÃºmero de telÃ©fono debe tener 10 dÃ­gitos");
                    return "redirect:/actualizacionUsuario";
                }
                phone = phoneClean; //Asignar el telefono limpio
            }
            //Verificar si el email ya estÃ¡ en uso por otro usuario
            if (!currentUser.getEmail().equals(email) &&
                    userRepository.findByEmail(email).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El email ya estÃ¡ registrado");
                return "redirect:/actualizacionUsuario";
            }
            User user = userRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            user.setEmail(email);
            user.setPhone(phone);
            userRepository.save(user);
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("success", "Contacto actualizado correctamente");
            return "redirect:/actualizacionUsuario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar contacto");
            return "redirect:/actualizacionUsuario";
        }
    }
    @PostMapping("/ActualizarContrasena")
    public String actualizarContrasena(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                redirectAttributes.addFlashAttribute("error", "Sesion expirada");
                return "redirect:/login";
            }
            // Determinar la pÃ¡gina de redirecciÃ³n segÃºn el rol
            String redirectPage = currentUser.getRole().getName().equals("Admin") ?
                "/actualizacionUsuarioAdmin" : "/actualizacionUsuario";
            // Validar que las contraseÃ±as nuevas coincidan
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseÃ±as no coinciden");
                return "redirect:" + redirectPage;
            }
            if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "ContraseÃ±a actual incorrecta");
                return "redirect:" + redirectPage;
            }
            User user = userRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("success", "ContraseÃ±a actualizada correctamente");
            return "redirect:" + redirectPage;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la contraseÃ±a");
            // En caso de error, intentar determinar el rol para redirigir correctamente
            User currentUser = (User) session.getAttribute("user");
            String redirectPage = (currentUser != null && currentUser.getRole().getName().equals("Admin")) ?
                "/actualizacionUsuarioAdmin" : "/actualizacionUsuario";
            return "redirect:" + redirectPage;
        }
    }
}

