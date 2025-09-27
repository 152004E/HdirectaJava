package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Entity.Role;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.RoleRepository;
import com.exe.Huerta_directa.Repository.UserRepository;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    //Metodo para obtener todos los usuarios en reporte excel
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
        User nuevoUser = userRepository.save(user);
        return convertirADTO(nuevoUser);
    }

    @Override
    public UserDTO actualizarUser(Long userId, UserDTO userDTO) {
        User userExistente = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado por id: " + userId));

        actualizarDatosPersona(userExistente, userDTO);
        User userActualizado = userRepository.save(userExistente);
        return convertirADTO(userActualizado);
    }

    @Override
    public void eliminarUserPorId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado por id: " + userId);
        } else {
            userRepository.deleteById(userId);
        }
    }

    //Convertir Entity a DTO
    private UserDTO convertirADTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        //Si la persona tiene un rol asignado, se convierte a DTO
        if (user.getRole() != null) {
            userDTO.setIdRole(user.getRole().getIdRole());
        } else {
            userDTO.setIdRole(null); // O cualquier valor por defecto que consideres apropiado
        }

        return userDTO;
    }

    // Convertir DTO a Entity
    private User convertirAEntity(UserDTO userDTO) {
        User user = new User();
        // Comentamos esta línea para que la base de datos genere el ID automáticamente
        // y evitemos el error de duplicidad.
        // user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        // Aquí buscamos el rol.
        if (userDTO.getIdRole() != null) {
            // Si el DTO tiene un rol asignado (idRole no es nulo), buscamos ese rol
            // específico en la base de datos.
            Role role = roleRepository.findById(userDTO.getIdRole())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + userDTO.getIdRole()));
            user.setRole(role); // Asignamos el rol encontrado al usuario.
        } else {
            // --- INICIO DE LA MODIFICACIÓN PARA ROL POR DEFECTO ---
            // Si el DTO no tiene un rol asignado (idRole es nulo), buscamos el rol por
            // defecto con ID 2 ('Cliente').
            // Este rol se asignará automáticamente a los nuevos usuarios si no se
            // especifica uno.
            Role defaultRole = roleRepository.findById(2L) // Buscamos el rol con ID 2. Usamos 2L para indicar que es un
                    // Long.
                    .orElseThrow(() -> new RuntimeException(
                            "Rol por defecto con ID 2 ('cliente') no encontrado. Por favor, asegúrate de que exista en la base de datos."));
            user.setRole(defaultRole); // Asignamos el rol por defecto encontrado al usuario.
            // --- FIN DE LA MODIFICACIÓN ---
        }

        return user;
    }

    //Actualizar Entity con datos del DTO
    private void actualizarDatosPersona(User user, UserDTO userDTO) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        // Si el DTO tiene un rol asignado, buscar el rol existente en la base de datos
        if (userDTO.getIdRole() != null) {
            Role role = roleRepository.findById(userDTO.getIdRole())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + userDTO.getIdRole()));
            user.setRole(role);
        } else {
            throw new RuntimeException("El idRole no puede ser nulo");
        }
    }

    // MÉTODO PARA EXPORTAR A EXCEL (Corregido)
    @Override
    public void exporUserstToExcel(OutputStream outputStream) throws IOException {
        //Un nuevo libro de excel
        Workbook workbook = new XSSFWorkbook();

        //Una nueva hoja en el libro
        Sheet sheet = workbook.createSheet("Users");

        //Crear la fila de encabezado
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("User ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Password");
        headerRow.createCell(4).setCellValue("Role"); // CORREGIDO: era índice 3, debe ser 4

        //Obtener todos los usuarios
        List<User> users = obtenerTodos();
        int rowNum = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue(user.getPassword());

            // Si el usuario tiene un rol asignado, obtener el nombre del rol; de lo contrario, dejarlo vacío
            String roleName = (user.getRole() != null) ? user.getRole().getName() : "No Role Assigned";
            row.createCell(4).setCellValue(roleName);
        }

        //Ajustamos el tamaño de las columnas automaticamente
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            //Escribimos el libro en el OutputStream proporcionado
            workbook.write(outputStream);
        } finally {
            //Cerramos el workbook para liberar recursos
            workbook.close();
        }
    }

    // MÉTODO PARA EXPORTAR A PDF 
    @Override
    public void exportUsersToPdf(OutputStream outputStream) throws IOException {
        Document document = new Document();

        try {

            PdfWriter.getInstance(document, outputStream);

            // Abrir el documento para empezar a escribir
            document.open();

            // Título principal con estilo
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

            // Información del reporte
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            
            List<User> users = obtenerTodos();
            Paragraph reportInfo = new Paragraph("Fecha de generación: " + currentDate + 
                    " | Total de registros: " + users.size(), infoFont);
            reportInfo.setAlignment(Element.ALIGN_RIGHT);
            reportInfo.setSpacingAfter(20);
            document.add(reportInfo);

            
            if (users.isEmpty()) {
                // Si no hay usuarios
                Font noDataFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.RED);
                Paragraph noData = new Paragraph("No se encontraron usuarios registrados.", noDataFont);
                noData.setAlignment(Element.ALIGN_CENTER);
                noData.setSpacingBefore(50);
                document.add(noData);
            } else {
                // Crear tabla con 5 columnas
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Configurar anchos de columnas
                float[] columnWidths = {1f, 2.5f, 3f, 2f, 1.5f};
                table.setWidths(columnWidths);

                // Estilo para encabezados
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
                
                // Agregar encabezados con estilo
                addTableHeader(table, "ID", headerFont);
                addTableHeader(table, "Nombre", headerFont);
                addTableHeader(table, "Email", headerFont);
                addTableHeader(table, "Contraseña", headerFont);
                addTableHeader(table, "Rol", headerFont);

                // Fuente para datos
                Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

                // Agregar datos de usuarios
                int rowCount = 0;
                for (User user : users) {
                    rowCount++;
                    
                    // Alternar colores de filas
                    Color rowColor = (rowCount % 2 == 0) ? new Color(240, 240, 240) : Color.WHITE;

                    // Agregar celdas con datos
                    addTableCell(table, String.valueOf(user.getId()), dataFont, rowColor, Element.ALIGN_CENTER);
                    addTableCell(table, user.getName() != null ? user.getName() : "N/A", dataFont, rowColor, Element.ALIGN_LEFT);
                    addTableCell(table, user.getEmail() != null ? user.getEmail() : "N/A", dataFont, rowColor, Element.ALIGN_LEFT);
                    addTableCell(table, "●●●●●●●●", dataFont, rowColor, Element.ALIGN_CENTER); // Ocultar contraseña por seguridad
                    
                    String roleName = (user.getRole() != null) ? user.getRole().getName() : "Sin Rol";
                    addTableCell(table, roleName, dataFont, rowColor, Element.ALIGN_CENTER);
                }

                document.add(table);

                // ==================== ESTADÍSTICAS ADICIONALES ====================
                
                // Agregar estadísticas por rol
                Map<String, Long> usersByRole = users.stream()
                        .collect(Collectors.groupingBy(
                                user -> user.getRole() != null && user.getRole().getName() != null 
                                        ? user.getRole().getName() 
                                        : "Sin Rol",
                                Collectors.counting()
                        ));

                if (!usersByRole.isEmpty()) {
                    document.add(new Paragraph(" ")); // Espacio

                    Font statsFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
                    Paragraph statsTitle = new Paragraph("Estadísticas por Rol:", statsFont);
                    statsTitle.setSpacingBefore(20);
                    document.add(statsTitle);

                    Font statsDataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
                    for (Map.Entry<String, Long> entry : usersByRole.entrySet()) {
                        Paragraph statLine = new Paragraph("• " + entry.getKey() + ": " + entry.getValue() + " usuario(s)", statsDataFont);
                        statLine.setIndentationLeft(20);
                        document.add(statLine);
                    }
                }
            }

            // ==================== PIE DE PÁGINA ====================
            
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.GRAY);
            Paragraph footer = new Paragraph("Reporte generado automáticamente por el sistema Huerta Directa", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

        } catch (DocumentException e) {
            throw new IOException("Error al crear el documento PDF: " + e.getMessage(), e);
        } finally {
            // Cerrar el documento
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    // MÉTODO AUXILIAR: Agregar encabezado de tabla con estilo
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

    // MÉTODO AUXILIAR: Agregar celda con datos y estilo
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

    // MÉTODO HEREDADO (manteniendo compatibilidad con tu código anterior)
    public void exportarUsersToPDF(OutputStream outputStream) throws IOException {
        // Redirigir al método mejorado
        exportUsersToPdf(outputStream);
    }
}