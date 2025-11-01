package com.exe.Huerta_directa.Repository;

import com.exe.Huerta_directa.Entity.Comment;
import com.exe.Huerta_directa.Entity.CommentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Buscar comentarios por el nombre del usuario
    List<Comment> findByUser_NameContainingIgnoreCase(String name);

    // Buscar comentarios por el email del usuario
    List<Comment> findByUser_EmailContainingIgnoreCase(String email);

    // Buscar comentarios de un usuario específico
    List<Comment> findByUser_Id(Long userId);

    // Buscar comentarios de un producto específico (campo correcto)
    List<Comment> findByProduct_IdProduct(Long productId);

    // Buscar comentarios por tipo (SITE, PRODUCT, etc.)
    List<Comment> findByCommentType(CommentType commentType);
}
