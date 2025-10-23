package com.exe.Huerta_directa.Controllers;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.exe.Huerta_directa.DTO.CommentDTO;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Service.CommentService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public RedirectView crearComentario(@RequestParam("commentCommenter") String commentCommenter,
            HttpSession session) {
        try {
            // ✅ Obtener usuario desde la sesión
            User userSession = (User) session.getAttribute("user");
            if (userSession == null) {
                // 🚫 Si no hay usuario en sesión, agregar mensaje de alerta y redirigir al
                // login
                return new RedirectView("/login?error=session&message=Debe+iniciar+sesión+para+dejar+un+comentario");
            }

            // ✅ PERMITIR A CUALQUIER USUARIO REGISTRADO DEJAR COMENTARIOS
            // (No solo admins, cualquier usuario autenticado puede dejar su opinión o
            // reseña)

            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentCommenter(commentCommenter);
            commentDTO.setCreationComment(LocalDate.now());
            commentDTO.setUserId(userSession.getId());

            commentService.crearComment(commentDTO, userSession.getId(), null);

            return new RedirectView("/Quienes_somos?success=¡Gracias+por+tu+comentario!");

        } catch (Exception e) {
            // 🚫 En caso de error, redirigir con mensaje de error
            e.printStackTrace();
            return new RedirectView("/Quienes_somos?error=No+se+pudo+enviar+tu+comentario,+inténtalo+de+nuevo");
        }

    }

}
