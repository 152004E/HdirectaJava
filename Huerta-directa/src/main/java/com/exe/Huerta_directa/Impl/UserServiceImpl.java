package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.RoleDTO;
import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Entity.Role;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.RoleRepository;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.RoleService;
import com.exe.Huerta_directa.Service.UserService;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> obtenerTodos() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> listarUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO obtenerUserPorId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado por id: " + userId));
        return convertirADTO(user);
    }

    @Override
    public UserDTO crearUser(UserDTO userDTO) {
        User user = convertirAEntity(userDTO);

        // ⭐ HASHEAR la contraseña antes de guardar
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(hashedPassword);
        }

        User nuevoUser = userRepository.save(user);
        return convertirADTO(nuevoUser);
    }

    @Override
    public UserDTO actualizarUser(Long userId, UserDTO userDTO) {
        User userExistente = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado por id: " + userId));

        actualizarDatosPersona(userExistente, userDTO);

        // ⭐ Solo hashear si la contraseña cambió (no está vacía)
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
            userExistente.setPassword(hashedPassword);
        }

        User userActualizado = userRepository.save(userExistente);
        return convertirADTO(userActualizado);
    }

    @Override
    @Transactional
    public void eliminarUserPorId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado por id: " + userId);
        }

        User user = userRepository.findById(userId).get();

        if (user.getProducts() != null && !user.getProducts().isEmpty()) {
            throw new RuntimeException("No se puede eliminar este usuario porque tiene " +
                    user.getProducts().size() + " producto(s) asociado(s). " +
                    "Elimine primero los productos o reasígnelos a otro usuario.");
        }

        userRepository.deleteById(userId);
    }

    // ⭐ MÉTODO DE AUTENTICACIÓN CON BCRYPT
    @Override
    public UserDTO autenticarUsuario(String email, String password) {
        // Buscar usuario por email
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return null;
        }

        // ⭐ Verificar la contraseña hasheada con BCrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        // Convertir a DTO y devolver
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(null); // NUNCA devolver la contraseña
        dto.setIdRole(user.getRole() != null ? user.getRole().getIdRole() : null);

        return dto;
    }

    @Override
    public UserDTO crearAdmin(UserDTO userDTO) {
        // Obtener el rol admin (id = 1)
        RoleDTO roleAdmin = roleService.obtenerRolePorId(1L);
        userDTO.setIdRole(roleAdmin.getIdRole());

        // ⭐ La contraseña se hasheará automáticamente en crearUser()
        return crearUser(userDTO);
    }

    // ========== MÉTODOS PRIVADOS ==========

    private UserDTO convertirADTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        // NO incluir la contraseña en el DTO
        userDTO.setPassword(null);
        userDTO.setCreacionDate(user.getCreacionDate());

        if (user.getRole() != null) {
            userDTO.setIdRole(user.getRole().getIdRole());
        } else {
            userDTO.setIdRole(null);
        }

        return userDTO;
    }

    private User convertirAEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Se hasheará en crearUser()

        // CORRECCIÓN: Establecer fecha actual si no existe
        if (user.getCreacionDate() == null) {
            user.setCreacionDate(LocalDate.now());
        }

        if (userDTO.getIdRole() != null) {
            Role role = roleRepository.findById(userDTO.getIdRole())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + userDTO.getIdRole()));
            user.setRole(role);
        } else {
            // Rol por defecto: Cliente (ID 2)
            Role defaultRole = roleRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException(
                            "Rol por defecto con ID 2 ('cliente') no encontrado."));
            user.setRole(defaultRole);
        }

        return user;
    }

    private void actualizarDatosPersona(User user, UserDTO userDTO) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        // La contraseña se hasheará en actualizarUser() si no está vacía
        // NO hashear aquí para evitar hashear dos veces

        if (userDTO.getIdRole() != null) {
            Role role = roleRepository.findById(userDTO.getIdRole())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + userDTO.getIdRole()));
            user.setRole(role);
        } else {
            throw new RuntimeException("El idRole no puede ser nulo");
        }
    }

    // ========== EXPORTACIÓN ==========

    @Override
    public void exporUserstToExcel(OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("User ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Password");
        headerRow.createCell(4).setCellValue("Role");

        List<User> users = obtenerTodos();
        int rowNum = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue("●●●●●●●●"); // ⭐ Ocultar contraseña por seguridad

            String roleName = (user.getRole() != null) ? user.getRole().getName() : "No Role Assigned";
            row.createCell(4).setCellValue(roleName);
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }
    }

    @Override
    public void exportUsersToPdf(OutputStream outputStream) throws IOException {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.GREEN.darker());
            Paragraph title = new Paragraph("HUERTA DIRECTA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Subtítulo
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
            Paragraph subtitle = new Paragraph("Reporte de Usuarios", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(10);
            document.add(subtitle);

            // Info del reporte
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            List<User> users = obtenerTodos();
            Paragraph reportInfo = new Paragraph("Fecha de generación: " + currentDate +
                    " | Total de registros: " + users.size(), infoFont);
            reportInfo.setAlignment(Element.ALIGN_RIGHT);
            reportInfo.setSpacingAfter(20);
            document.add(reportInfo);

            if (users.isEmpty()) {
                Font noDataFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.RED);
                Paragraph noData = new Paragraph("No se encontraron usuarios registrados.", noDataFont);
                noData.setAlignment(Element.ALIGN_CENTER);
                noData.setSpacingBefore(50);
                document.add(noData);
            } else {
                // Tabla
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                float[] columnWidths = { 1f, 2.5f, 3f, 2f, 1.5f };
                table.setWidths(columnWidths);

                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);

                addTableHeader(table, "ID", headerFont);
                addTableHeader(table, "Nombre", headerFont);
                addTableHeader(table, "Email", headerFont);
                addTableHeader(table, "Contraseña", headerFont);
                addTableHeader(table, "Rol", headerFont);

                Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

                int rowCount = 0;
                for (User user : users) {
                    rowCount++;
                    Color rowColor = (rowCount % 2 == 0) ? new Color(240, 240, 240) : Color.WHITE;

                    addTableCell(table, String.valueOf(user.getId()), dataFont, rowColor, Element.ALIGN_CENTER);
                    addTableCell(table, user.getName() != null ? user.getName() : "N/A", dataFont, rowColor,
                            Element.ALIGN_LEFT);
                    addTableCell(table, user.getEmail() != null ? user.getEmail() : "N/A", dataFont, rowColor,
                            Element.ALIGN_LEFT);
                    addTableCell(table, "●●●●●●●●", dataFont, rowColor, Element.ALIGN_CENTER); // ⭐ Ocultar

                    String roleName = (user.getRole() != null) ? user.getRole().getName() : "Sin Rol";
                    addTableCell(table, roleName, dataFont, rowColor, Element.ALIGN_CENTER);
                }

                document.add(table);

                // Estadísticas
                Map<String, Long> usersByRole = users.stream()
                        .collect(Collectors.groupingBy(
                                user -> user.getRole() != null && user.getRole().getName() != null
                                        ? user.getRole().getName()
                                        : "Sin Rol",
                                Collectors.counting()));

                if (!usersByRole.isEmpty()) {
                    document.add(new Paragraph(" "));

                    Font statsFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
                    Paragraph statsTitle = new Paragraph("Estadísticas por Rol:", statsFont);
                    statsTitle.setSpacingBefore(20);
                    document.add(statsTitle);

                    Font statsDataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
                    for (Map.Entry<String, Long> entry : usersByRole.entrySet()) {
                        Paragraph statLine = new Paragraph(
                                "• " + entry.getKey() + ": " + entry.getValue() + " usuario(s)", statsDataFont);
                        statLine.setIndentationLeft(20);
                        document.add(statLine);
                    }
                }
            }

            // Footer
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY);
            Paragraph footer = new Paragraph("Reporte generado automáticamente por el sistema Huerta Directa",
                    footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

        } catch (DocumentException e) {
            throw new IOException("Error al crear el documento PDF: " + e.getMessage(), e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private void addTableHeader(PdfPTable table, String headerTitle, Font font) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(new Color(67, 160, 71));
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(headerTitle, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setPadding(8);
        table.addCell(header);
    }

    private void addTableCell(PdfPTable table, String text, Font font, Color backgroundColor, int alignment) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(5);
        cell.setBorderWidth(1);
        table.addCell(cell);
    }

    public void exportarUsersToPDF(OutputStream outputStream) throws IOException {
        exportUsersToPdf(outputStream);
    }
}