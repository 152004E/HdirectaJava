package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Entity.Role;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.RoleRepository;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository; 

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
            userDTO.setIdRole(user.getRole().getName());
        }else{
            userDTO.setIdRole(null);
        }

        return userDTO;
    }

    //Convertir DTO a Entity
    private User convertirAEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        //Si el DTO tiene un rol asignado, buscar el rol existente en la base de datos
        if (userDTO.getIdRole() != null && !userDTO.getIdRole().isEmpty()) {
            Role role = roleRepository.findByName(userDTO.getIdRole());
            if (role != null) {
                user.setRole(role);
            }
            // Si el rol no existe, podrías lanzar una excepción o log del error
        }

        return user;
    }

    //Actualizar Entity con datos del DTO
    private void actualizarDatosPersona(User user, UserDTO userDTO) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        // Si el DTO tiene un rol asignado, buscar el rol existente en la base de datos
        if (userDTO.getIdRole() != null && !userDTO.getIdRole().isEmpty()) {
            Role role = roleRepository.findByName(userDTO.getIdRole());
            if (role != null) {
                user.setRole(role);
            }
            // Si el rol no existe, podrías lanzar una excepción o log del error
        } else {
            user.setRole(null); // O mantener el rol actual, según la lógica de negocio
        }
    }

}
