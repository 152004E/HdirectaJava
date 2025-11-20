package com.exe.Huerta_directa.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.exe.Huerta_directa.DTO.CommentDTO;
import com.exe.Huerta_directa.Entity.Comment;
import com.exe.Huerta_directa.Entity.CommentType;
import com.exe.Huerta_directa.Entity.Product;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.CommentRepository;
import com.exe.Huerta_directa.Repository.ProductRepository;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.CommentService;

import jakarta.transaction.Transactional;

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
        Comment comment = commentRepository.findById(idComment)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con el id " + idComment));

        return convertirADTO(comment);
    }

    // 🔹 Crear un nuevo comentario (relacionado con usuario y/o producto)
    @Transactional
    @Override
    public CommentDTO crearComment(CommentDTO commentDTO, Long userId, Long productId) {
        // Buscar el usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id " + userId));

        // Convertir el DTO a entidad
        Comment comment = convertirAEntity(commentDTO);
        comment.setUser(user); // Asocia el comentario con el usuario

        // Si el comentario está relacionado con un producto
        if (productId != null) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con el id " + productId));
            comment.setProduct(product);
            comment.setCommentType(CommentType.PRODUCT);
        } else {
            // Si no hay producto, es un comentario general del sitio
            comment.setCommentType(CommentType.SITE);
        }

        // Guardar el comentario
        Comment newComment = commentRepository.save(comment);

        // Retornar DTO
        return convertirADTO(newComment);
    }

    @Override
    public List<Comment> obtenerComentariosPorTipo(CommentType commentType) {
        return commentRepository.findByCommentType(commentType);
    }

    // 🔹 Actualizar un comentario existente
    @Transactional
    @Override
    public CommentDTO actualizarComment(Long idComment, CommentDTO commentDTO) {
        Comment commmentExistente = commentRepository.findById(idComment)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con el id " + idComment));

        actualizarDatosComment(commmentExistente, commentDTO);
        Comment commentActializado = commentRepository.save(commmentExistente);
        return convertirADTO(commentActializado);
    }

    // 🔹 Eliminar un comentario por su ID
    @Transactional
    @Override
    public void eliminarComment(Long idComment) {
        if (!commentRepository.existsById(idComment)) {
            throw new RuntimeException("El comentario con el id :" + idComment + " No se puedo encontrar");
        } else {
            commentRepository.deleteById(idComment);
        }

    }

    @Override
    public List<CommentDTO> listarCommentsPorProducto(Long productId) {
        return commentRepository.findByProduct_IdProduct(productId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> listarCommentsPorUsuario(Long userId) {
        return commentRepository.findByUser_Id(userId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Convertir Entity a DTO
    private CommentDTO convertirADTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setIdComment(comment.getIdComment());
        commentDTO.setCommentCommenter(comment.getCommentCommenter());
        commentDTO.setCreationComment(comment.getCreationComment());
        commentDTO.setCommentType(comment.getCommentType());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setProductId(
                comment.getProduct() != null ? comment.getProduct().getIdProduct() : null);

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

    private Comment convertirAEntity(CommentDTO commentDTO) {
        Comment commentEntity = new Comment();
        commentEntity.setIdComment(commentDTO.getIdComment());
        commentEntity.setCommentCommenter(commentDTO.getCommentCommenter());
        commentEntity.setCreationComment(commentDTO.getCreationComment());
        commentEntity.setCommentType(commentDTO.getCommentType());
        commentEntity.setUser(userRepository.findById(commentDTO.getUserId()).orElse(null));
        if (commentDTO.getProductId() != null) {
            commentEntity.setProduct(
                    productRepository.findById(commentDTO.getProductId()).orElse(null));
        }

        return commentEntity;
    }
    // Actualizar Entity con datos del DTO

    private void actualizarDatosComment(Comment comment, CommentDTO commentDTO) {
        comment.setCommentCommenter(commentDTO.getCommentCommenter());
        comment.setCreationComment(commentDTO.getCreationComment());

        if (commentDTO.getUserId() != null) {
            User user = userRepository.findById(commentDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + commentDTO.getUserId()));
            comment.setUser(user);
        }
    }
        //Este servicio es para implementarlo en el front de dashboard comentarios

    @Override
    public List<Comment> obtenerComentariosPorUsuario(Long userId) {
        return commentRepository.findByUser_Id(userId);
    }

    @Override
    public List<Comment> listarTodosComments() {
        return commentRepository.findAll();
    }



}
