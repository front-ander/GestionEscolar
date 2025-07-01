package com.registro2.CRUD.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.registro2.CRUD.model.Estudiante;
import com.registro2.CRUD.services.EstudianteReporteService;

@Controller
public class ReporteController {
    @Autowired
    private EstudianteReporteService estudianteReporteService;

    @GetMapping("/reporte/estudiantes")
    public String mostrarReporteEstudiantes(Model model) {
        List<Estudiante> estudiantes = estudianteReporteService.obtenerTodosEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "reporte-estudiantes";
    }

    @GetMapping("/reporte/estudiantes/pdf")
    public ResponseEntity<byte[]> exportarEstudiantesPDF() {
        try {
            List<Estudiante> estudiantes = estudianteReporteService.obtenerTodosEstudiantes();
            
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Colores personalizados
            BaseColor primaryColor = new BaseColor(52, 152, 219); // Azul
            BaseColor secondaryColor = new BaseColor(236, 240, 241); // Gris claro
            BaseColor accentColor = new BaseColor(46, 204, 113); // Verde
            
            // Encabezado con logo y título
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 3});
            
            // Logo/Icono (texto estilizado)
            Font logoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, primaryColor);
            Paragraph logo = new Paragraph("🎓", logoFont);
            logo.setAlignment(Element.ALIGN_CENTER);
            
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setBackgroundColor(secondaryColor);
            logoCell.setPadding(10);
            headerTable.addCell(logoCell);
            
            // Título principal
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.WHITE);
            Paragraph title = new Paragraph("SISTEMA DE GESTIÓN ACADÉMICA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            
            PdfPCell titleCell = new PdfPCell(title);
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setBackgroundColor(primaryColor);
            titleCell.setPadding(15);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerTable.addCell(titleCell);
            
            document.add(headerTable);
            document.add(new Paragraph(" ")); // Espacio
            
            // Subtítulo del reporte
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, accentColor);
            Paragraph subtitle = new Paragraph("📊 Reporte de Estudiantes", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            
            // Información del reporte
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph info = new Paragraph("Generado el: " + java.time.LocalDate.now() + " | Total de estudiantes: " + estudiantes.size(), infoFont);
            info.setAlignment(Element.ALIGN_CENTER);
            document.add(info);
            document.add(new Paragraph(" ")); // Espacio
            
            // Tabla principal
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{0.5f, 1.5f, 1.5f, 2f, 1f, 1f});
            
            // Encabezados de tabla con estilo
            String[] columnHeaders = {"ID", "Nombre", "Apellido", "Email", "Curso", "Código"};
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            
            for (String header : columnHeaders) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(primaryColor);
                headerCell.setPadding(8);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(headerCell);
            }
            
            // Datos con estilo alternado
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            boolean alternateRow = false;
            
            for (Estudiante estudiante : estudiantes) {
                BaseColor rowColor = alternateRow ? secondaryColor : BaseColor.WHITE;
                
                // ID
                PdfPCell idCell = new PdfPCell(new Phrase(String.valueOf(estudiante.getId()), dataFont));
                idCell.setBackgroundColor(rowColor);
                idCell.setPadding(6);
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);
                
                // Nombre
                PdfPCell nombreCell = new PdfPCell(new Phrase(estudiante.getNombre(), dataFont));
                nombreCell.setBackgroundColor(rowColor);
                nombreCell.setPadding(6);
                table.addCell(nombreCell);
                
                // Apellido
                PdfPCell apellidoCell = new PdfPCell(new Phrase(estudiante.getApellido(), dataFont));
                apellidoCell.setBackgroundColor(rowColor);
                apellidoCell.setPadding(6);
                table.addCell(apellidoCell);
                
                // Email
                PdfPCell emailCell = new PdfPCell(new Phrase(estudiante.getEmail(), dataFont));
                emailCell.setBackgroundColor(rowColor);
                emailCell.setPadding(6);
                table.addCell(emailCell);
                
                // Curso
                PdfPCell cursoCell = new PdfPCell(new Phrase(estudiante.getCurso(), dataFont));
                cursoCell.setBackgroundColor(rowColor);
                cursoCell.setPadding(6);
                cursoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cursoCell);
                
                // Código
                PdfPCell codigoCell = new PdfPCell(new Phrase(estudiante.getCodigoEstudiante(), dataFont));
                codigoCell.setBackgroundColor(rowColor);
                codigoCell.setPadding(6);
                codigoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(codigoCell);
                
                alternateRow = !alternateRow;
            }
            
            document.add(table);
            document.add(new Paragraph(" ")); // Espacio
            
            // Pie de página
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY);
            Paragraph footer = new Paragraph("© 2025 Gestion Academica-DevFronted ", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
            
            document.close();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "reporte_estudiantes_" + java.time.LocalDate.now() + ".pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 