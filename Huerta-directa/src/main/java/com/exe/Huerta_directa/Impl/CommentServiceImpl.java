package com.exe.Huerta_directa.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exe.Huerta_directa.DTO.CommentDTO;
import com.exe.Huerta_directa.Entity.Comment;
import com.exe.Huerta_directa.Repository.CommentRepository;
import com.exe.Huerta_directa.Repository.ProductRepository;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository,
            ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // 🔹 Listar todos los comentarios
    @Override
    public List<CommentDTO> listarComments() {
        return commentRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    // 🔹 Obtener un comentario por su ID

    @Override
    public CommentDTO obtenerCommentPorId(Long idComment) {
        return null ;
    }

    // 🔹 Crear un nuevo comentario (relacionado con usuario y/o producto)
    @Override
    public CommentDTO crearComment(CommentDTO commentDTO, Long userId, Long productId) {
        return null ;
    }

    // 🔹 Actualizar un comentario existente
    @Override
    public CommentDTO actualizarComment(Long idComment, CommentDTO commentDTO) {
        return null ;
    }

    // 🔹 Eliminar un comentario por su ID
    @Override
    public void eliminarComment(Long idComment) {
        
    }

    // 🔹 Listar comentarios por producto (si los comentarios están asociados a
    // productos)
    @Override
    public List<CommentDTO> listarCommentsPorProducto(Long productId) {
        return null ;
    }

    // 🔹 Listar comentarios por usuario
    @Override
    public List<CommentDTO> listarCommentsPorUsuario(Long userId) {
        return null ;
    }

    private CommentDTO convertirADTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setIdComment(comment.getIdComment());
        commentDTO.setEmailCommenter(comment.getEmailCommenter());
        commentDTO.setCommentCommenter(comment.getCommentCommenter());
        commentDTO.setCreationComment(comment.getCreationComment());

        /*
         * private Long idComment;
         * private String NameCommenter;
         * private String EmailCommenter;
         * private String CommentCommenter;
         * private LocalDate CreationComment;
         * private Long userId;
         * private Long productId;
         */

        return commentDTO;

    }

}
