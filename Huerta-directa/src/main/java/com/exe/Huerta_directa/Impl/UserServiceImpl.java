package com.exe.Huerta_directa.Impl;

import com.exe.Huerta_directa.DTO.UserDTO;
import com.exe.Huerta_directa.Service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    public List<UserDTO> listarUsers() {
        return List.of();
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
}
