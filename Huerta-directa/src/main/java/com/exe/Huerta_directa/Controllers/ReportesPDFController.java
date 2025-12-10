package com.exe.Huerta_directa.Controllers;

import com.exe.Huerta_directa.DTO.ProductDTO;
import com.exe.Huerta_directa.Service.ProductService;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.Color; // <--- ESTE ES EL IMPORT CORRECTO PARA COLOR
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReportesPDFController {

    private final ProductService productService;
    private final ResourceLoader resourceLoader;

    @GetMapping("/reportes/categorias/pdf")
    public void exportarPDF(HttpServletResponse response) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_productos.pdf");

        Document doc = new Document(PageSize.A4.rotate(), 40, 40, 40, 40);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        // TITULO PRINCIPAL
        Color colorVerde = new Color(60, 130, 40);
        Font tituloGreen = new Font(Font.HELVETICA, 26, Font.BOLD, colorVerde);

        Paragraph titulo = new Paragraph("HUERTA DIRECTA", tituloGreen);
        titulo.setAlignment(Element.ALIGN_CENTER);

        Font sub = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph subtitulo = new Paragraph("Reporte de Productos por Categoría", sub);
        subtitulo.setAlignment(Element.ALIGN_CENTER);

        doc.add(titulo);
        doc.add(subtitulo);

        // Fecha
        Paragraph fecha = new Paragraph(
                "Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                new Font(Font.HELVETICA, 12));
        fecha.setAlignment(Element.ALIGN_CENTER);
        doc.add(fecha);

        doc.add(new Paragraph("\n"));
        // IMAGEN
        try {
            Resource recurso = resourceLoader.getResource("classpath:static/graficos/productosPorCategoria.png");

            if (recurso.exists()) {
                byte[] imgBytes = recurso.getInputStream().readAllBytes();
                Image grafico = Image.getInstance(imgBytes);
                grafico.scaleToFit(600, 400);
                grafico.setAlignment(Element.ALIGN_CENTER);
                doc.add(grafico);
                doc.add(new Paragraph("\n\n"));
            } else {
                Paragraph aviso = new Paragraph("⚠ No se encontró el gráfico.");
                aviso.setAlignment(Element.ALIGN_CENTER);
                doc.add(aviso);
            }

        } catch (IOException e) {
            Paragraph aviso = new Paragraph("⚠ Error cargando imagen: " + e.getMessage());
            aviso.setAlignment(Element.ALIGN_CENTER);
            doc.add(aviso);
        }

        doc.add(new Paragraph("\n"));

        // TABLA PRODUCTOS
        List<ProductDTO> productos = productService.listarProducts();

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[] { 3f, 3f, 2f });

        // Color del encabezado
        Color verdeHeader = new Color(72, 140, 60);

        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

        String[] headers = { "Nombre", "Categoría", "Unidad" };

        for (String h : headers) {
            PdfPCell celda = new PdfPCell(new Phrase(h, headerFont));
            celda.setBackgroundColor(verdeHeader);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(8);
            table.addCell(celda);
        }

        // Alternancia de colores
        boolean gris = false;
        Color grisClaro = new Color(230, 230, 230);

        for (ProductDTO p : productos) {

            Color bg = gris ? grisClaro : Color.WHITE;
            gris = !gris;

            table.addCell(celdaNormal(p.getNameProduct(), bg));
            table.addCell(celdaNormal(p.getCategory(), bg));
            table.addCell(celdaNormal(p.getUnit(), bg));
        }

        doc.add(table);
        doc.add(new Paragraph("\n"));

        // TOTALES POR CATEGORIA

        Paragraph subt2 = new Paragraph("Totales por Categoría", new Font(Font.HELVETICA, 18, Font.BOLD));
        subt2.setAlignment(Element.ALIGN_CENTER);
        doc.add(subt2);

        doc.add(new Paragraph("\n"));

        Map<String, Long> totales = productService.contarProductosPorCategoria();

        for (Map.Entry<String, Long> e : totales.entrySet()) {
            Paragraph linea = new Paragraph(e.getKey() + ": " + e.getValue(), new Font(Font.HELVETICA, 14));
            linea.setAlignment(Element.ALIGN_CENTER);
            doc.add(linea);
        }

        doc.close();
    }

    // CELDA SIN AMBIGÜEDADES

    private PdfPCell celdaNormal(String texto, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(texto != null ? texto : "N/A"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(bg);
        cell.setPadding(6);
        return cell;
    }
}
