package com.exe.Huerta_directa.Controllers;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;

import com.exe.Huerta_directa.DTO.CommentDTO;
import com.exe.Huerta_directa.Entity.Comment;
import com.exe.Huerta_directa.Entity.CommentType;
import com.exe.Huerta_directa.Entity.User;
import com.exe.Huerta_directa.Service.CommentService;

import org.jfree.chart.ChartUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


import jakarta.servlet.http.HttpSession;

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
        List<CommentDTO> comments = commentService.listarCommentsPorUsuario(userSession.getId());

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

    @GetMapping("/reporteFc")
    public String reporteProductVsSite(Model model) {

        // Traer todos los comentarios
        List<Comment> listaComments = commentService.listarTodosComments();
        model.addAttribute("comentarios", listaComments);

        // Contar comentarios por tipo
        int totalProductComments = 0;
        int totalSiteComments = 0;

        for (Comment c : listaComments) {
            if (c.getCommentType() == CommentType.PRODUCT) {
                totalProductComments++;
            } else if (c.getCommentType() == CommentType.SITE) {
                totalSiteComments++;
            }
        }

        // Crear dataset para el gráfico
        DefaultPieDataset<String> datos = new DefaultPieDataset<>();
        datos.setValue("Comentarios PRODUCT", totalProductComments);
        datos.setValue("Comentarios SITE", totalSiteComments);

        // Crear gráfico
        JFreeChart chart = ChartFactory.createPieChart(
                "Comparación Comentarios PRODUCT vs SITE",
                datos,
                true,
                true,
                false
        );

        // Guardar gráfico en disco
        String rutaArchivo = "uploads/graficos/reporteProductSite.png";

        try {
            File carpeta = new File("uploads/graficos/");
            if (!carpeta.exists()) carpeta.mkdirs();

            try (OutputStream out = new FileOutputStream(rutaArchivo)) {
                ChartUtils.writeChartAsPNG(out, chart, 650, 450);
            }

            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Agregar atributos para la vista
        model.addAttribute("grafico", "/uploads/graficos/reporteProductSite.png");
        model.addAttribute("totalProductComments", totalProductComments);
        model.addAttribute("totalSiteComments", totalSiteComments);

        return "Reportes_estadisticos/commentFc";
    }


}
