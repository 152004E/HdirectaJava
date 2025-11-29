package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.DTO.UserDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public interface UserService {

    List<UserDTO> listarUsers();

    UserDTO obtenerUserPorId(Long userId);

    UserDTO crearUser(UserDTO userDTO);

    UserDTO actualizarUser(Long userId, UserDTO userDTO);

    void eliminarUserPorId(Long userId);

    void exporUserstToExcel(OutputStream outputStream) throws IOException;

    void exportUsersToPdf(OutputStream outputStream) throws IOException;

    //método para login
    UserDTO autenticarUsuario(String email, String password);
    //obtener id para admin
    UserDTO crearAdmin(UserDTO userDTO);


    // Nuevo: obtener teléfono o DTO por email
   // String obtenerPhonePorEmail(String email);
    //UserDTO obtenerUserPorEmail(String email);
}

