package com.exe.Huerta_directa.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.CommentEvent;

import com.exe.Huerta_directa.DTO.CommentDTO;
import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Entity.Comment;
import com.exe.Huerta_directa.Entity.Product;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Repository.CommentRepository;
import com.exe.Huerta_directa.Repository.ProductRepository;
import com.exe.Huerta_directa.Repository.UserRepository;
import com.exe.Huerta_directa.Service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    // private final ProductRepository productRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository,
            ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        // this.productRepository = productRepository;
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
    @Override
    public CommentDTO crearComment(CommentDTO commentDTO, Long userId, Long productId) {
        // buscar el usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id " + userId));

        // Covertir el comentario a entidad
        Comment comment = convertirAEntity(commentDTO);
        comment.setUser(user); //le asigamos el comentario al user

        Comment newComment = commentRepository.save(comment);

        return convertirADTO(newComment);
    }

    // 🔹 Actualizar un comentario existente
    @Override
    public CommentDTO actualizarComment(Long idComment, CommentDTO commentDTO) {
     //   Comment commmentExistente = commentRepository.findById(idComment).orElseThrow(()-> new RuntimeException("Comentario no encontrado con el id "+ idComment));


        return null;
    }

    // 🔹 Eliminar un comentario por su ID
    @Override
    public void eliminarComment(Long idComment) {

    }

    // 🔹 Listar comentarios por producto (si los comentarios están asociados a
    // productos)
    @Override
    public List<CommentDTO> listarCommentsPorProducto(Long productId) {
        return null;
    }

    // 🔹 Listar comentarios por usuario
    @Override
    public List<CommentDTO> listarCommentsPorUsuario(Long userId) {
        return null;
    }

    // Convertir Entity a DTO
    private CommentDTO convertirADTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setIdComment(comment.getIdComment());
        commentDTO.setEmailCommenter(comment.getEmailCommenter());
        commentDTO.setCommentCommenter(comment.getCommentCommenter());
        commentDTO.setCreationComment(comment.getCreationComment());
        commentDTO.setNameCommenter(comment.getNameCommenter());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setProductId(comment.getProduct().getIdProduct());

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
        commentEntity.setEmailCommenter(commentDTO.getEmailCommenter());
        commentEntity.setCommentCommenter(commentDTO.getCommentCommenter());
        commentEntity.setCreationComment(commentDTO.getCreationComment());
        commentEntity.setNameCommenter(commentDTO.getNameCommenter());
        commentEntity.setUser(userRepository.findById(commentDTO.getUserId()).orElse(null));
        // commentEntity.setProduct(productRepository.findById(commentDTO.getProductId()).orElse(null));

        return commentEntity;
    }
    // Actualizar Entity con datos del DTO
    /* 
    private void actualizarDatosProducto(Product product, ProductDTO productDTO) {
        product.setNameProduct(productDTO.getNameProduct());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setImageProduct(productDTO.getImageProduct());
        product.setUnit(productDTO.getUnit());
        product.setDescriptionProduct(productDTO.getDescriptionProduct());
        product.setPublicationDate(productDTO.getPublicationDate());

        if (productDTO.getUserId() != null) {
            User user = userRepository.findById(productDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + productDTO.getUserId()));
            product.setUser(user);
        }
    }
        */

}
