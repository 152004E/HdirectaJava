package com.exe.Huerta_directa.Service;

import com.exe.Huerta_directa.DTO.CommentDTO;
import java.util.List;

public interface CommentService {

    // 🔹 Listar todos los comentarios
    List<CommentDTO> listarComments();

    // 🔹 Obtener un comentario por su ID
    CommentDTO obtenerCommentPorId(Long idComment);

    // 🔹 Crear un nuevo comentario (relacionado con usuario y/o producto)
    CommentDTO crearComment(CommentDTO commentDTO, Long userId, Long productId);

    // 🔹 Actualizar un comentario existente
    CommentDTO actualizarComment(Long idComment, CommentDTO commentDTO);

    // 🔹 Eliminar un comentario por su ID
    void eliminarComment(Long idComment);

    // 🔹 Listar comentarios por producto (si los comentarios están asociados a productos)
    List<CommentDTO> listarCommentsPorProducto(Long productId);

    // 🔹 Listar comentarios por usuario
    List<CommentDTO> listarCommentsPorUsuario(Long userId);
}