package com.exe.Huerta_directa.Repository;

import com.exe.Huerta_directa.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 🔹 Buscar comentarios por el nombre del comentarista
    List<Comment> findByNameCommenterContainingIgnoreCase(String nameCommenter);

    // 🔹 Buscar comentarios por email del comentarista
    List<Comment> findByEmailCommenterContainingIgnoreCase(String emailCommenter);

    // 🔹 Buscar comentarios de un usuario específico
    List<Comment> findByUser_Id(Long userId);

    // 🔹 (Opcional) Buscar comentarios de un producto específico
    List<Comment> findByProduct_Id(Long productId);
}
