package com.exe.Huerta_directa.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model; // ✅ IMPORT CORRECTO

import com.exe.Huerta_directa.DTO.CommentDTO;
import com.exe.Huerta_directa.Entity.Comment;
import com.exe.Huerta_directa.Entity.CommentType;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Service.CommentService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
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

    // ✅ GET — Mostrar comentarios tipo SITE (para la página "quienes somos")
    @GetMapping("/Quienes_somos")
    public String mostrarComentariosSitio(Model model) {
        // Obtener lista de comentarios tipo SITE
        List<Comment> comments = commentService.obtenerComentariosPorTipo(CommentType.SITE);

        // Agregar lista al modelo para Thymeleaf
        model.addAttribute("comments", comments);

        // Renderiza la plantilla Quienes_somos.html
        return "Quienes_somos/quienes_somos";
    }

    @GetMapping("/MensajesComentarios")
    public String mostrarMensajesComentarios(Model model, HttpSession session) {
        // Obtener usuario de la sesión
        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            return "redirect:/login?error=session&message=Debe+iniciar+sesión";
        }

        // Obtener todos los comentarios o filtrar por usuario según necesites
        List<Comment> comments = commentService.obtenerComentariosPorUsuario(userSession.getId());

        // O si quieres todos: commentService.obtenerTodosLosComentarios();

        // Añadir comentarios al modelo
        model.addAttribute("comments", comments);

        return "DashBoard/MensajesComentarios";
    }

    @GetMapping("/deleteComment/{id}")
    public RedirectView eliminarComentario(@PathVariable Long id) {
        commentService.eliminarComment(id);
        return new RedirectView("/MensajesComentarios?deleted=true");
    }

    @GetMapping("/editComment/{id}")
    public String editarComentario(@PathVariable Long id, Model model) {
        CommentDTO comment = commentService.obtenerCommentPorId(id);
        model.addAttribute("comment", comment);
        return "DashBoard/EditarComentario";
    }

}
