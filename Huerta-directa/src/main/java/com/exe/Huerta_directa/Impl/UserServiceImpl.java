package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.UserDTO;

import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> listarUsers() {

             return null;


    }

    @Override
    public UserDTO obtenerUserPorId(Long userId) {
        return null;
    }

    @Override
    public UserDTO crearUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO actualizarUser(Long userId, UserDTO userDTO) {
        return null;
    }

    @Override
    public void eliminarUserPorId(Long userId) {

    }

    /*Convertir Entity a DTO
    private UserDTO convertirADTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        //Si la persona tiene un rol asignado, se convierte a DTO
        if (user.getRole() != null) {
            userDTO.setId(User.getRole().getId());
        } else {
            userDTO.setId(null);
        }

        return userDTO;
    }*/

}
