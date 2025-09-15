package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.DTO.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> listarUsers();

    UserDTO obtenerUserPorId(Long userId);

    UserDTO crearUser(UserDTO userDTO);

    UserDTO actualizarUser(Long userId, UserDTO userDTO);

    void eliminarUserPorId(Long userId);
}
