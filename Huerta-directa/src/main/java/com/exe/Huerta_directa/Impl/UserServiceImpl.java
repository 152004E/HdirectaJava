package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Entity.Role;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.RoleRepository;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.UserService;



import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {



    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
    public UserDTO crearUser (UserDTO userDTO) {
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

    //Metodo para exportar a excel
    public void  exporUserstToExcel(OutputStream outputStream)  throws IOException {

        //Un nuevo libro de excel
        Workbook workbook = new XSSFWorkbook();

        //Una nueva hoja en el libro
        Sheet sheet = workbook.createSheet("Users");

        //Crear la fila de encabezado
        Row headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Password");
        headerRow.createCell(3).setCellValue("Role");
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

}
